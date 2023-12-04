package Thread;

import Server.Address;

import java.util.List;
import java.util.Map;

/**
 * This thread will decide if its children should be sent to a new server, or should just be run in this server.
 */
public class DistributedThread extends Thread {

    // Server address + capacity
    Map<Address, Integer> otherServers;


    public DistributedThread(Map<Address, Integer> servers) {
        super();
    }

}