package Thread;

import Server.Address;
import Server.Server;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This thread will decide if its children should be sent to a new server, or should just be run in this server.
 */
public abstract class DistributedThread<Input, Output> {

    protected Output defaultValue;
    protected DistributedThread<Input, Output> parent;
    protected Thread thisThread;
    protected Output data;
    protected Output result;
    protected List<DistributedThread<Input, Output>> children;

    // Server address + capacity
    private Map<Address, Integer> otherServers;
    private int totalCapacity;
    protected DistributedRunnable<Input, Output> function;

    protected CombineFunction<Output> combine;
    private ThreadFactory<Input, Output> threadFactory;

    public DistributedThread(DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        this(null, new HashMap<>(), func, combine, data, defaultValue);
    }

    public DistributedThread(DistributedThread<Input, Output> parent, DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        this(parent, new HashMap<>(), func, combine, data, defaultValue);
    }

    public DistributedThread(DistributedThread<Input, Output> parent, Map<Address, Integer> servers, DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        this.parent = parent;
        otherServers = new HashMap<>();
        otherServers.putAll(servers);
        totalCapacity = calculateCapacity();
        function = func;
        function.setData(data);
        function.setThisThread(this);
        thisThread = new Thread(function);
        children = new ArrayList<>();
        this.combine = combine;
        this.defaultValue = defaultValue;
    }

    private int calculateCapacity() {
        int totalCapacity = Server.getInstance().getCapacity();

        for (int capacity : otherServers.values())
            totalCapacity += capacity;

        return totalCapacity;
    }

    public void runThread() {
        thisThread.start();
    }

    /**
     * Should be called after a join, otherwise the results will not be accurate.
     * @return The number of TOTAL children in the thread tree
     */
    public int getTotalChildren() {
        return children.stream().reduce(children.size(), (acc, child) -> acc + child.getTotalChildren(), Integer::sum);
    }

    public DistributedThread<Input, Output> createThread(List<Input> data) {
        // TODO: add server splitting
        Class<? extends DistributedRunnable> cl = function.getClass();
        Constructor co;
        DistributedRunnable<Input, Output> newFunc;
        try {
            co = cl.getConstructor();
            newFunc = (DistributedRunnable) co.newInstance();
        } catch (Exception e) {
            System.err.println("Your class does not have a constructor");
            throw new RuntimeException(e);
        }

        DistributedThread<Input, Output> child;

        if (otherServers != null && !otherServers.isEmpty()) {
            Address otherServer = otherServers.keySet().stream().findFirst().get();
            int split = (otherServers.size() - 1)/2;
            List<Map.Entry<Address, Integer>> entries = new ArrayList<>(otherServers.entrySet());

            Map<Address, Integer> otherServerHelpers = new ArrayList<>(entries.subList(split, otherServers.size())).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            child = threadFactory.createThread(this, otherServerHelpers, newFunc, combine, data, defaultValue, Mode.ExternalThread);

            ((ExternalThread<Input, Output>) child).setAddress(otherServer);

        } else {

            child = threadFactory.createThread(this, otherServers, newFunc, combine, data, defaultValue);
        }
        children.add(child);

        return child;
    }

    public void setThreadFactory(ThreadFactory<Input, Output> threadFactory) {
        this.threadFactory = threadFactory;
    }

    public ThreadFactory<Input, Output> getThreadFactory() {
        return threadFactory;
    }
}