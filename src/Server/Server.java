package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static Server server;

    private static final int PORT = 8000;
    ServerSocket socket;

    private Server() throws IOException {
        socket = new ServerSocket(PORT);
    }

    public static Server getInstance() throws IOException {
        if (server == null) {
            return new Server();
        }
        return server;
    }

}
