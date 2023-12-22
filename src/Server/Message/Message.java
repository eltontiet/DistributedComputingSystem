package Server.Message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public String message;

    public Message() {

    }

    public Message(String message) {
        this.message = message;
    }
}
