package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketStreamObject {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public SocketStreamObject(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket=socket;
        this.ois = ois;
        this.oos = oos;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

}
