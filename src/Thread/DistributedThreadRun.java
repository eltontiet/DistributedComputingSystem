package Thread;

import Server.Address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributedThreadRun<Input, Output> extends DistributedThread<Input, Output> {

    public DistributedThreadRun(DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        this(null, new HashMap<>(), func, combine, data, defaultValue);
    }

    public DistributedThreadRun(Map<Address, Integer> servers, DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        this(null, servers, func, combine, data, defaultValue);
    }

    public DistributedThreadRun(DistributedThread<Input, Output> parent, Map<Address, Integer> servers, DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        super(parent, servers, func, combine, data, defaultValue);
        setThreadFactory(new ThreadFactory<Input, Output>(Mode.Normal));
    }

    @Override
    public void runThread() {
        thisThread.start();
    }

    public Output joinThread() {
        Output val = defaultValue;

        try {
            thisThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (DistributedThread<Input, Output> thread : children) {
            val = combine.combine(val, ((DistributedThreadRun<Input, Output>) thread).joinThread());
        }

        data = function.getOutput();

        val = combine.combine(val, data);

        result = val;

        return result;
    }
}
