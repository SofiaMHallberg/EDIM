package client;

import server.Activity;
import server.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class manages the communication between the Client classes and the Server classes.
 * @autor Carolin Nordström & Oscar Kareld.
 * @version 1.0
 */

public class ClientCommunicationController {
    private ClientController clientController;
    private Buffer<User> userBuffer;
    private Buffer<Activity> activityBuffer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket; // Detta är en socket. Det är ff en socket.
    private int port;
    private String ip, className = "Class: ClientCommunicationController, Method: ";

    /**
     * Receives a clientController object and then try to connect with the server. Constructs a buffer,
     * ClientSender and a ClientReceiver Object. Then starts two Threads.
     * @param clientController The received ClientController object.
     */
    public ClientCommunicationController(ClientController clientController) {
        this.clientController = clientController;
        connect();
        userBuffer = new Buffer<>();
        ClientSender clientSender = new ClientSender();
        clientSender.start();
        new ClientReceiver().start();
    }

    /**
     * Tries to create a new socket and connect to the server's IP.
     */
    public void connect() {
        try {
            socket = new Socket("127.0.0.1", 3343);
            System.out.println(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method lays a User object in a UserBuffer which mission is to be sent to the server.
     * @param user the object to be sent.
     */
    public void sendUser(User user) {
        userBuffer.put(user);
    }

    /**
     * This method lays a Activity object in a ActivityBuffer which mission is to be sent to the server.
     * @param activity the object to be sent.
     */
    public void sendActivity(Activity activity) {
        activityBuffer.put(activity);
    }

    /**
     * This method tries to close the socket and the connection to the server.
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ClientSender starts a new thread which retrieves an object from a buffer and sends it to the server.
    private class ClientSender extends Thread {

        /**
         * Tries to construct a OutPutStream.
         */
        public ClientSender() {
            try {
                if (oos == null) {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println(className + "ClientSenders konstruktor");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * A thread which retrieves an object from the buffer and then writes it to the stream.
         */
        public void run() {
            while (true) {
                try {
                    User user = userBuffer.get();
                    System.out.println("Nu skickas användarobjektet till servern" + user.toString());
                    oos.writeObject(user);
                    System.out.println("Nu har användarobjektet skickats till servern" + user.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ClientReceiver extends Thread {

        /**
         * Tries to open an Input Stream then tries to read an object from the stream.
         * Then checks the object's class value and sends it to {@link ClientController}.
         */
        public void run() {
            while (ois == null) {
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(className + "ClientReceiver run()");
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            while (true) {
                try {
                    sleep(500);
                    System.out.println("ClientReceiver, precis innan if-satsen" + ois);



                    Object object = ois.readObject();

                    System.out.println("ois: " + object);
                    if (object instanceof User) {
                        System.out.println("Inne i första if-satsen");
                        User user = (User) object;
                        clientController.receiveExistingUser(user);
                        System.out.println("Skickat till ClientController");
                    }
                    else if (object instanceof Activity) {
                        Activity activity = (Activity) object;
                        clientController.receiveNotificationFromCCC(activity);
                    }
                    else if (object instanceof String) {
                        System.out.println("String-objekt");
                        String message = (String) object;
                        clientController.receiveAcceptedUser(message);

                    }
                    else System.out.println("Den gick inte in i någon if-sats :(");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
