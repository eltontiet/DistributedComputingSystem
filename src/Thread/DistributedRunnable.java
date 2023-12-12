package Thread;

import java.util.List;

public abstract class DistributedRunnable<Input, Output> implements Runnable {
    private ThreadFactory<Input, Output> threadFactory;
    public abstract Output func(List<Input> data);

    public void setThreadFactory(ThreadFactory<Input, Output> threadFactory) {
        this.threadFactory = threadFactory;
    }

    public ThreadFactory<Input, Output> getThreadFactory() {
        return threadFactory;
    }
}
