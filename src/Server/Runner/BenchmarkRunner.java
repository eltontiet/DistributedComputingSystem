package Server.Runner;

import Thread.DistributedRunnable;
import Thread.CombineFunction;
import Thread.DistributedThreadBenchmark;

import java.util.List;

public class BenchmarkRunner<Input, Output> {
    Class<? extends DistributedRunnable<Input, Output>> function;
    Class<? extends CombineFunction<Output>> combine;

    public BenchmarkRunner(Class<? extends DistributedRunnable<Input, Output>> func, Class<? extends CombineFunction<Output>> combine) {
        this.function = func;
        this.combine = combine;
    }

    public int runProgram(List<Input> data, Output defaultData) {
        try {
            DistributedThreadBenchmark<Input, Output> run = new DistributedThreadBenchmark<Input, Output>(function.getConstructor().newInstance(), combine.getConstructor().newInstance(), data, defaultData);
            run.runThread();
            return run.joinThreads();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
