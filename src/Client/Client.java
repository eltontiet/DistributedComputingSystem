package Client;

import Thread.DistributedRunnable;
import Thread.CombineFunction;
import Thread.DistributedThreadRun;

import java.util.List;

public class Client<Input, Output> {

    Class<? extends DistributedRunnable<Input, Output>> function;
    Class<? extends CombineFunction<Output>> combine;

    public Client(Class<? extends DistributedRunnable<Input, Output>> func, Class<? extends CombineFunction<Output>> combine) {
        this.function = func;
        this.combine = combine;
    }

    public Output runProgram(List<Input> data, Output defaultData) {
        try {
            DistributedThreadRun<Input, Output> run = new DistributedThreadRun<Input, Output>(function.getConstructor().newInstance(), combine.getConstructor().newInstance(), data, defaultData);
            run.runThread();
            return run.joinThread();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
