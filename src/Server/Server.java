package Server;

import Server.Compiler.CodeCompiler;
import Server.Message.*;
import Server.Runner.CodeRunner;
import Server.Runner.Functions;
import Thread.DistributedRunnable;
import Thread.CombineFunction;
import Thread.ExternalThread;


import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server {
    private static Server server;
    private static final int PORT = 8000;

    private ServerSocket socket;
    private int capacity;
    private CodeRunner currentRun;
    private Map<Address, ExternalServer> otherServers;
    private Map<Address, Integer> capacities;
    private Map<Address, Functions> otherServersFunctions;
    private Map<Address, Thread> running;
    private Map<Address, ExternalThread> children;
    private Map<Address, Object> responses;


    private Server() throws IOException {
        socket = new ServerSocket(PORT);
        otherServers = new HashMap<>();
        otherServersFunctions = new HashMap<>();
        children = new HashMap<>();
        responses = new HashMap<>();
        capacity = runBenchmark();
        capacities = new HashMap<>();
        new AcceptRequest().start();
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

    public InetAddress getAddress() throws UnknownHostException {
        return Inet4Address.getLocalHost();
    }

    // TODO: Run a quick multi-threaded calculation and create a value from it
    private int runBenchmark() {
        return 1;
    }


    public void connectToServer(InetAddress address) {
        try {
            Socket connectionSocket = new Socket();

            System.out.println("Connecting to server: " + address);

            connectionSocket.connect(new InetSocketAddress(address, 8000));

            System.out.println("Connected to server: " + address);

            addServer(connectionSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int getCapacity() {
        return capacity;
    }

    private void addServer(Socket socket) {
        Address address = new Address(socket.getInetAddress());
        otherServers.put(address, new ExternalServer(socket, address));
    }

    private void acceptRequest(ClientRequest message) {

    }

    public void setRunnable(InetAddress inetAddress, RunnableFunctionCode code) {
        Address address = new Address(inetAddress);

        Class<? extends DistributedRunnable> className = (Class<? extends DistributedRunnable>) CodeCompiler.compile(address.getName() + ".runnable", code.message); // TODO add sequence number

        Functions functions = otherServersFunctions.getOrDefault(address, new Functions());
        functions.setRunnable(className);
        otherServersFunctions.put(address, functions);
        tryRun(functions, address);
    }

    public void setRunnable(InetAddress inetAddress, DistributedRunnable<?, ?> obj) {
        Address address = new Address(inetAddress);

        Functions functions = otherServersFunctions.getOrDefault(address, new Functions());
        functions.setRunnable(obj.getClass());
        otherServersFunctions.put(address, functions);
        tryRun(functions, address);
    }

    public void setCombineFunction(InetAddress inetAddress, CombineFunctionCode code) {
        Address address = new Address(inetAddress);

        Class<? extends CombineFunction> className = (Class<? extends CombineFunction>) CodeCompiler.compile(address.getName() + ".combine", code.message); // TODO add sequence number

        Functions functions = otherServersFunctions.getOrDefault(address, new Functions());
        functions.setCombine(className);
        otherServersFunctions.put(address, functions);
        tryRun(functions, address);
    }

    public void setCombineFunction(InetAddress inetAddress, CombineFunction<?> obj) {
        Address address = new Address(inetAddress);

        Functions functions = otherServersFunctions.getOrDefault(address, new Functions());
        functions.setCombine(obj.getClass());
        otherServersFunctions.put(address, functions);
        tryRun(functions, address);
    }

    public void setData(InetAddress inetAddress, List<?> obj) {
        Address address = new Address(inetAddress);
        Functions functions = otherServersFunctions.getOrDefault(address, new Functions());
        functions.setData(obj);
        otherServersFunctions.put(address, functions);
        tryRun(functions, address);
    }

    public void setDefaultValue(InetAddress inetAddress, Object obj) {
        Address address = new Address(inetAddress);
        Functions functions = otherServersFunctions.getOrDefault(address, new Functions());
        functions.setDefaultValue(obj);
        otherServersFunctions.put(address, functions);
        tryRun(functions, address);
    }

    public void tryRun(Functions functions, Address address) {
        if (functions.canRun()) {
            Thread t = new RunFunction(functions, address);
            running.put(address, t);
            t.start();
        }
    }

    public Object run(Class<? extends DistributedRunnable> function, Class<? extends CombineFunction> combine, List data, Object defaultValue) {
        capacities.clear();
        for (Address a : otherServers.keySet()) {
            capacities.put(a, 1);
        }
        CodeRunner cr = new CodeRunner(function, combine);
        return cr.runProgram(data, defaultValue, capacities);

    }

    private void finishRun(Address address, Object res) {
        running.remove(address);
        otherServersFunctions.remove(address);
        Response response = new Response();
        response.response = res;
        otherServers.get(address).sendMessage(response);
    }

    public void collectResponse(InetAddress inetAddress, Response message) {
        Address address = new Address(inetAddress);

        responses.put(address, message.response);

    }

    public void sendRequest(Address address, ExternalThread externalThread) {
        ExternalServer otherServer = otherServers.get(address);
        otherServer.sendObject(externalThread.getRunnable());
        otherServer.sendObject(externalThread.getCombine());
        otherServer.sendObject(externalThread.getData());
        otherServer.sendObject(externalThread.getDefaultValue());
    }

    public void waitForResponse(Address address, ExternalThread externalThread) {
        while (!responses.containsKey(address)) {
            Thread.yield();
        }
    }

    public Object getResponse(Address address, ExternalThread externalThread) {
        return responses.get(address);
    }

    class AcceptRequest extends Thread {
        @Override
        public void run() {
            try {
                Socket server = socket.accept();
                System.out.println("Received connection request from server: " + server.getInetAddress());
                addServer(server);
            } catch (IOException e) {
                System.err.println("An exception was caught while accepting server requests\n" + e.getMessage());
            }
        }
    }

    class RunFunction extends Thread {
        CodeRunner cr;
        Address address;
        List<?> data;
        Object defaultValue;

        RunFunction(Functions functions, Address address) {
            cr = new CodeRunner(functions.getRunnable(), functions.getCombine());
            this.address = address;
            data = functions.getData();
            defaultValue = functions.getDefaultValue();
        }

        @Override
        public void run() {
            System.out.println("Running Program.");
            Object res = cr.runProgram(data, defaultValue);
            finishRun(address, res);
        }
    }
}
