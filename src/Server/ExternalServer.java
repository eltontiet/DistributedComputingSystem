package Server;

import Server.Message.CombineFunctionCode;
import Server.Message.Message;
import Server.Message.Response;
import Server.Message.RunnableFunctionCode;
import Thread.DistributedRunnable;
import Thread.CombineFunction;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ExternalServer {
    Socket socket;
    ObjectInputStream reader;
    ObjectOutputStream writer;

    public ExternalServer(Socket socket, Address address) {
        this.socket = socket;
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new ReceiveMessages().start();
    }

    public boolean sendMessage(Message message) {
        for (int i = 0; i < 5; i++) {
            try {
                writer.writeObject(message);
                return true;
            } catch (IOException ignored) {

            }
        }
        return false;
    }

    private void handleMessage(Message message) {
        if (message instanceof CombineFunctionCode) {
            Server.getInstance().setCombineFunction(socket.getInetAddress(), (CombineFunctionCode) message);
        } else if (message instanceof RunnableFunctionCode) {
            Server.getInstance().setRunnable(socket.getInetAddress(), (RunnableFunctionCode) message);
        } else if (message instanceof Response) {
            Server.getInstance().collectResponse(socket.getInetAddress(), (Response) message);
        }
    }

    public void sendObject(Object obj) {
        try {
            writer.writeObject(obj);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    class ReceiveMessages extends Thread {
        @Override
        public void run() {
            while (true) {
                Object obj;
                try {
                    obj = reader.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if (Message.class.isAssignableFrom(obj.getClass())) {
                    handleMessage((Message) obj);
                } else if (List.class.isAssignableFrom(obj.getClass())) {
                    Server.getInstance().setData(socket.getInetAddress(), (List<?>) obj);
                } else if (DistributedRunnable.class.isAssignableFrom(obj.getClass())) {
                    Server.getInstance().setRunnable(socket.getInetAddress(), (DistributedRunnable<?, ?>) obj);
                } else if (CombineFunction.class.isAssignableFrom(obj.getClass())) {
                    Server.getInstance().setCombineFunction(socket.getInetAddress(), (CombineFunction<?>) obj);
                } else {
                    Server.getInstance().setDefaultValue(socket.getInetAddress(), obj);
                }
            }
        }
    }
}
