package Thread;

import Server.Address;
import Server.Server;

import java.util.List;
import java.util.Map;

public class ExternalThread<Input, Output> extends DistributedThreadRun<Input, Output> {

    private Address address;

    public ExternalThread(DistributedThread<Input, Output> parent, Map<Address, Integer> servers, DistributedRunnable<Input, Output> func, CombineFunction<Output> combine, List<Input> data, Output defaultValue) {
        super(parent, servers, func, combine, data, defaultValue);
    }

    @Override
    public void runThread() {
        Server.getInstance().sendRequest(address, this);
    }

    @Override
    public Output joinThread() { // TODO: This will block if other server fails
        Server.getInstance().waitForResponse(address, this);
        return (Output) Server.getInstance().getResponse(address, this);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DistributedRunnable getRunnable() {
        return function;
    }

    public CombineFunction getCombine() {
        return combine;
    }

    public List<Input> getData() {
        return function.getData();
    }

    public Output getDefaultValue() {
        return defaultValue;
    }
}
