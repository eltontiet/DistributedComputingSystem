package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static Server server;
    private static final int PORT = 8000;

    private ServerSocket socket;
    private int capacity;

    private Server() throws IOException {
        socket = new ServerSocket(PORT);
        capacity = runBenchmark();
    }

    public static Server getInstance() {
        if (server == null) {
            try {
                server = new Server();
                return server;
            } catch (Exception e) {
                System.err.println("Server could not start:\n" + e);
                System.exit(-1);
            }
        }
        return server;
    }

    // TODO: Run a quick multi-threaded calculation and create a value from it
    private int runBenchmark() {
        return 1;
    }

    public int getCapacity() {
        return capacity;
    }
}
