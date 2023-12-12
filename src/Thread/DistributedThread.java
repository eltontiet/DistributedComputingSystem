package Thread;

import Server.Address;
import Server.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This thread will decide if its children should be sent to a new server, or should just be run in this server.
 */
public abstract class DistributedThread<Input, Output> {

    protected DistributedThread<Input, Output> parent;
    protected Thread thisThread;
    protected Output data;
    protected List<DistributedThreadBenchmark<Input, Output>> children;

    // Server address + capacity
    private Map<Address, Integer> otherServers;
    private int totalCapacity;
    protected DistributedRunnable<Input, Output> function;

    public DistributedThread(DistributedRunnable<Input, Output> func, List<Input> data) {
        this(null, new HashMap<>(), func, data);
    }

    public DistributedThread(DistributedThread<Input, Output> parent, DistributedRunnable<Input, Output> func, List<Input> data) {
        this(parent, new HashMap<>(), func, data);
    }

    public DistributedThread(DistributedThread<Input, Output> parent, Map<Address, Integer> servers, DistributedRunnable<Input, Output> func, List<Input> data) {
        this.parent = parent;
        otherServers = new HashMap<>();
        otherServers.putAll(servers);
        totalCapacity = calculateCapacity();
        function = func;
    }

    private int calculateCapacity() {
        int totalCapacity = Server.getInstance().getCapacity();

        for (int capacity : otherServers.values())
            totalCapacity += capacity;

        return totalCapacity;
    }

    public abstract void runThread();

}