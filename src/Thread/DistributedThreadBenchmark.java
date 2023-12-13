package Thread;

import java.util.List;

/**
 * Runs a benchmark of the thread function to gather information about the number of splits in the function,
 */
public class DistributedThreadBenchmark<Input, Output> extends DistributedThread<Input, Output> {

    protected int numSplits;

    public DistributedThreadBenchmark(DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        this(null, func, combine, data, defaultValue);
    }

    public DistributedThreadBenchmark(DistributedThread<Input, Output> parent, DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        super(parent, func, combine, data, defaultValue);
        setThreadFactory(new ThreadFactory<Input, Output>(Mode.Benchmark));
    }

    /**
     * Waits for all of the children threads to finish running and collects the data;
     * @return The number of new threads this thread and its children created.
     */
    // TODO: Maybe create class to return more info, this function will return the benchmark and aggregate the data from runs below.
    public int joinThreads() {
        int numSplits = 0;
        for (DistributedThread<Input, Output> thread : children) {
            numSplits += ((DistributedThreadBenchmark<Input, Output>) thread).joinThreads();
        }

        try {
            thisThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        numSplits += this.numSplits;

        return numSplits;
    }
}
