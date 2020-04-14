package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class creates a thread pool and handles the communication from the Client.
 * @autor Sofia Hallberg & Chanon Borgström.
 * @version 1.0
 */

public class ConnectionServer {
    private ServerController serverController;
    private ServerSocket serverSocket;
    private int port;
    private ObjectInputStream ois;
    private String className="Package: Server. Class: ConnectionServer ";
    private LinkedList<ReceiverThread> threadPool;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private Buffer<User> onLineBuffer;

    /**
     * Receives all necessary data and starts the server and then generates and starts the thread pool.
     * @param serverController received controller object.
     * @param port received port number.
     * @param socketHashMap received socket HashMap.
     * @param onLineBuffer received online buffer.
     */
    public ConnectionServer(ServerController serverController, int port, HashMap<String, SocketStreamObject> socketHashMap, Buffer<User> onLineBuffer) {
        this.serverController=serverController;
        this.port=port;
        this.socketHashMap=socketHashMap;
        this.onLineBuffer=onLineBuffer;
        this.threadPool=new LinkedList<>();
        startServer();
        generateThreadPool(20);
        startThreadPool();
    }

    /**
     * Creates a server socket.
     */
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a thread pool with the received int.
     * @param nbrOfConnections received number of connections.
     */
    public void generateThreadPool(int nbrOfConnections) {
        for(int i=0; i< nbrOfConnections; i++) {
            threadPool.add(new ReceiverThread());
        }

    }

    /**
     * Starts the thread pool.
     */
    public void startThreadPool() {
        for (ReceiverThread thread:threadPool)
            thread.start();

    }

    // Inner Thread class: creates a connection and sends it forth to the ClientHandler class.
    private class ReceiverThread extends Thread {
        private String className="Package: Server. Class: ReceiverThread ";

        /**
         * creates a connection and sends it forth to the ClientHandler class.
         */
        public void run() {
            while(!Thread.interrupted()) {
              //  System.out.println(className+"thread has started");
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

    // Inner Thread Class: creates the streams and receives data from the client.
    private class ClientHandler extends Thread {
        private SocketStreamObject socketStreamObject;
        private String className="Package: Server. Class: ClientHandler ";

        /**
         * Creates a Socket Stream Object with the received socket.
         * @param socket the received object.
         */
        public ClientHandler(Socket socket) {
            socketStreamObject=new SocketStreamObject(socket);
            start();
        }

        /**
         * creates the input and output streams and then receives an object.
         * Checks its value and send it to {@link ServerController}.
         */
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
            Activity activity;
            UserType userType;
            String userName="";

            while(!Thread.interrupted()) {
                try {
                    ObjectInputStream ois = socketStreamObject.getOis();
                    Object object = ois.readObject();
                    if(object instanceof User) {
                        user = (User) object;
                        userName = user.getUserName();
                        userType = user.getUserType();


                        switch (userType) {
                            case LOGIN:
                                socketHashMap.put(userName, socketStreamObject);
                                onLineBuffer.put(user);
                                System.out.println(className + "user login");
                                break;
                            case LOGOUT:
                                socketHashMap.remove(userName);
                                onLineBuffer.put(user);
                                System.out.println(className + "user logout");
                                interrupt();
                                break;
                        }
                    }else {
                        activity = (Activity) object;
                        System.out.println(className+"received activity: " + activity.getActivityName());
                        //TODO: Hantera inkommande aktiviteter.
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
