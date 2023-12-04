package Thread;

import java.util.List;

/**
 * Runs a benchmark of the thread function to gather information about the number of splits in the function,
 */
public class DistributedThreadBenchmark<Input, Output> extends DistributedThread<Input, Output> {

    List<DistributedThreadBenchmark<Input, Output>> children;

    public DistributedThreadBenchmark(DistributedRunnable<Input, Output> func, List<Input> data) {
        super(func, data);
    }

    // TODO: Maybe create class to return more info, this function will return the benchmark and aggregate the data from runs below.
    public Output run() {
        return 1;
    }


    private void createThread(List<Input> newData) {
        new DistributedThreadBenchmark<>(function, );
    }

    private int joinThreads() {
        return 1;
    }
}
