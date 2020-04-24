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
 *
 * @version 1.0
 * @autor Sofia Hallberg & Chanon Borgström.
 */

public class ReceiverServer {
    private ServerSocket serverSocket;
    private ServerController serverController;
    private int port;
    private String className = "Class: ConnectionServer ";
    private LinkedList<ReceiverThread> threadPool;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private Buffer<User> loginLogoutBuffer;
    private Buffer receiveBuffer;

    /**
     * Receives all necessary data and starts the server and then generates and starts the thread pool.
     *
     * @param port          received port number.
     * @param socketHashMap received socket HashMap.
     * @param loginLogoutBuffer  received online buffer.
     */
    public ReceiverServer(ServerController serverController, int port, HashMap<String, SocketStreamObject> socketHashMap, Buffer<User> loginLogoutBuffer, Buffer receiveBuffer) {
        this.serverController = serverController;
        this.port = port;
        this.socketHashMap = socketHashMap;
        this.loginLogoutBuffer = loginLogoutBuffer;
        this.threadPool = new LinkedList<>();
        this.receiveBuffer = receiveBuffer;
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
     *
     * @param nbrOfConnections received number of connections.
     */
    public void generateThreadPool(int nbrOfConnections) {
        for (int i = 0; i < nbrOfConnections; i++) {
            threadPool.add(new ReceiverThread());
        }

    }

    /**
     * Starts the thread pool.
     */
    public void startThreadPool() {
        for (ReceiverThread thread : threadPool)
            thread.start();

    }

    // Inner Thread class: creates a connection and sends it forth to the ClientHandler class.
    private class ReceiverThread extends Thread {
        private String className = "Package: Server. Class: ReceiverThread ";

        /**
         * creates a connection and sends it forth to the ClientHandler class.
         */
        public void run() {
            while (!Thread.interrupted()) {
                Socket socket;
                try {
                    socket = serverSocket.accept();
                    System.out.println(className + "Received socket: " + socket);
                    new ClientHandler(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Inner Thread Class: creates the streams and receives data from the client.
    private class ClientHandler extends Thread {
        private SocketStreamObject socketStreamObject;
        private String className = "Class: ClientHandler ";

        /**
         * Creates a Socket Stream Object with the received socket.
         *
         * @param socket the received object.
         */
        public ClientHandler(Socket socket) {
            socketStreamObject = new SocketStreamObject(socket);
            start();
        }

        /**
         * creates the input and output streams and then receives an object.
         * Checks its value and send it to {@link ServerController}.
         */
        public void run() {
            Socket socket = socketStreamObject.getSocket();

            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                socketStreamObject.setOis(ois);
                socketStreamObject.setOos(oos);
            } catch (IOException e) {
                e.printStackTrace();
            }

            User user = null;
            Activity activity;
            UserType userType;
            String userName = "";

            while (!Thread.interrupted()) {
                try {
                    ObjectInputStream ois = socketStreamObject.getOis();
                    Object object = ois.readObject();
                    if (object instanceof User) {
                        user = (User) object;
                        userName = user.getUserName();
                        userType = user.getUserType();


                        switch (userType) {
                            case LOGIN:
                                socketHashMap.put(userName, socketStreamObject);
                                loginLogoutBuffer.put(user);
                                System.out.println(className + "user login");
                                break;
                            case LOGOUT:
                                socketHashMap.remove(userName);
                                loginLogoutBuffer.put(user);
                                System.out.println(className + "user logout");
                                interrupt();
                                break;
                            case COMPLETEDACTIVITY:
                                break;
                            case SENDINTERVAL:
                                serverController.setTimeInterval(user.getUserName(),user.getNotificationInterval());
                                break;

                        }
                    } else {
                        activity = (Activity) object;
                        System.out.println(className + "received activity: " + activity.getActivityName());

                        //TODO: Hantera inkommande aktiviteter.
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
