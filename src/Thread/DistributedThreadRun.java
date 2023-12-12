package Thread;

import Server.Address;

import java.util.List;
import java.util.Map;

public class DistributedThreadRun<Input, Output> extends DistributedThread<Input, Output> {
    public DistributedThreadRun(DistributedThread<Input, Output> parent, Map<Address, Integer> servers, DistributedRunnable<Input, Output> func, List<Input> data) {
        super(parent, servers, func, data);
    }

    @Override
    public void runThread() {

    }
}
