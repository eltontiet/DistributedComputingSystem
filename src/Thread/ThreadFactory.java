package Thread;

import Server.Address;

import java.util.List;
import java.util.Map;

public class ThreadFactory<Input, Output> {

    private Mode mode;

    public ThreadFactory() {
        mode = Mode.Normal;
    }

    public ThreadFactory(Mode mode) {
        this.mode = mode;
    }

    // TODO: create overload methods
    public DistributedThread<Input, Output> createThread(DistributedThread<Input, Output> parent, Map<Address, Integer> servers,
                                                         DistributedRunnable<Input,Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        switch (mode) {
            case Normal:
                return new DistributedThreadRun<Input, Output>(parent, servers, func, combine, data, defaultValue);
            case Benchmark:
                return new DistributedThreadBenchmark<Input, Output>(parent, func, combine, data, defaultValue);
            default:
                return null; // TODO: fix
        }
    }
}
