package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class ConnectionServer {
    private ServerController serverController;
    private ServerSocket serverSocket;
    private int port;
    private ObjectInputStream ois;
    private String className="Package: Server. Class: ConnectionServer ";
    private LinkedList<ReceiverThread> threadPool;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private Buffer<User> onLineBuffer;

    public ConnectionServer(ServerController serverController, int port, HashMap<String, SocketStreamObject> socketHashMap, Buffer<User> onLineBuffer) {
        this.serverController=serverController;
        this.port=port;
        this.socketHashMap=socketHashMap;
        this.onLineBuffer=onLineBuffer;
        this.threadPool=new LinkedList<>();
        startServer();
        generateThreadPool(1);
        startThreadPool();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(className+": method startServer is executed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateThreadPool(int nbrOfConnections) {
        for(int i=0; i< nbrOfConnections; i++) {
            threadPool.add(new ReceiverThread());
        }
        System.out.println(className+"ThreadPool generated");
    }

    public void startThreadPool() {
        for (ReceiverThread thread:threadPool)
            thread.start();
        System.out.println(className+"ThreadPool threads are started");
    }

    private class ReceiverThread extends Thread {
        private String className="Package: Server. Class: ReceiverThread ";

        public void run() {
            while(!Thread.interrupted()) {
                System.out.println(className+"thread has started");
                Socket socket;
                try {
                    socket=serverSocket.accept();
                    System.out.println(className+"Received socket: "+socket);
                    ClientHandler clientHandler=new ClientHandler(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ClientHandler extends Thread {
        private SocketStreamObject socketStreamObject;
        private String className="Package: Server. Class: ClientHandler ";

        public ClientHandler(Socket socket) {
            socketStreamObject=new SocketStreamObject(socket);
            start();
        }

        public void run() {
            Socket socket=socketStreamObject.getSocket();

            try {
                ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                socketStreamObject.setOis(ois);
                socketStreamObject.setOos(oos);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            User user=null;
            UserType userType;
            String userName="";

            while(!Thread.interrupted()) {
                try {
                    System.out.println(className + "thread is listening");
                    ObjectInputStream ois = socketStreamObject.getOis();
                    System.out.println(className + ois);
                    user = (User) ois.readObject();
                    System.out.println(className + user.getUserName());
                    userName=user.getUserName();
                    System.out.println(className+"user received: "+userName);
                    userType=user.getUserType();

                    switch (userType) {
                        case LOGIN:
                            socketHashMap.put(userName, socketStreamObject);
                            onLineBuffer.put(user);
                            System.out.println(className+"user login");
                            break;
                        case LOGOUT:
                            socketHashMap.remove(userName);
                            onLineBuffer.put(user);
                            System.out.println(className+"user logout");
                            interrupt();
                            break;
                        case COMPLETEDACTIVITY:
                            //TODO vad händer när användaren skickar en uppdatering om att en aktivitet är utförd
                            break;
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
