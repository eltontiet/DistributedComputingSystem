package Thread;

import java.util.List;

public abstract class DistributedRunnable<Input, Output> implements Runnable {
    private DistributedThread<Input, Output> thisThread;
    Output output;
    private List<Input> data;
    public abstract Output func(List<Input> data);

    public DistributedRunnable() {

    }

    public Output getOutput() {
        return output;
    }

    public void setData(List<Input> data) {
        this.data = data;
    }

    public void setThisThread(DistributedThread<Input, Output> thread) {
        thisThread = thread;
    }

    @Override
    public void run() {

        output = func(data);
    }

    protected DistributedThread<Input, Output> createThread(List<Input> data) {
        DistributedThread<Input, Output> thread = thisThread.createThread(data);
        thread.runThread();
        return thread;
    }
}
