package Thread;

import java.util.List;

/**
 * Runs a benchmark of the thread function to gather information about the number of splits in the function,
 */
public class DistributedThreadBenchmark<Input, Output> extends DistributedThread<Input, Output> {

    protected int numSplits;

    public DistributedThreadBenchmark(DistributedRunnable<Input, Output> func, List<Input> data) {
        super(func, data);
        func.setThreadFactory(new ThreadFactory<Input, Output>(Mode.Benchmark));
    }

    public DistributedThreadBenchmark(DistributedThread<Input, Output> parent, DistributedRunnable<Input, Output> func, List<Input> data) {
        super(parent, func, data);
    }

    @Override
    public void runThread() {
        thisThread = new Thread(function);
        thisThread.start();
    }

    /**
     * Waits for all of the children threads to finish running and collects the data;
     * @return The number of new threads this thread and its children created.
     */
    // TODO: Maybe create class to return more info, this function will return the benchmark and aggregate the data from runs below.
    public int joinThreads() {
        int numSplits = 0;
        for (DistributedThreadBenchmark<Input, Output> thread : children) {
            numSplits += thread.joinThreads();
        }

        try {
            thisThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        numSplits += this.numSplits;

        return numSplits;
    }

    /**
     * Should be called after a join, otherwise the results will not be accurate.
     * @return The number of TOTAL children in the thread tree
     */
    public int getTotalChildren() {
        return children.stream().reduce(children.size(), (acc, child) -> acc + child.getTotalChildren(), Integer::sum);
    }
}
