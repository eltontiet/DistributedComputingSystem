package Server.Runner;

import Server.Address;
import Thread.DistributedRunnable;
import Thread.CombineFunction;
import Thread.DistributedThreadRun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeRunner<Input, Output> {
    Class<? extends DistributedRunnable<Input, Output>> function;
    Class<? extends CombineFunction<Output>> combine;

    public CodeRunner(Class<? extends DistributedRunnable<Input, Output>> func, Class<? extends CombineFunction<Output>> combine) {
        this.function = func;
        this.combine = combine;
    }

    public Output runProgram(List<Input> data, Output defaultData) {
        return runProgram(data, defaultData, new HashMap<>());
    }

    public Output runProgram(List<Input> data, Output defaultData, Map<Address, Integer> servers) {
        try {
            DistributedThreadRun<Input, Output> run = new DistributedThreadRun<Input, Output>(function.getConstructor().newInstance(), combine.getConstructor().newInstance(), data, defaultData);
            run.runThread();
            return run.joinThread();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
