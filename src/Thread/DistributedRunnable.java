package Thread;

public abstract class DistributedRunnable<T, U> implements Runnable {

    /**
     * Number of new threads per function call
      */
    private int estimatedSplits;

    public int getEstimatedSplits() {
        return estimatedSplits;
    }

    public void setEstimatedSplits() {

    }
}
