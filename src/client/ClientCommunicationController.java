package client;

import server.Activity;
import server.User;
import server.UserType;

import javax.swing.*;
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
    private Socket socket;
    private String className = "Class: ClientCommunicationController, Method: ";

    /**
     * Receives a clientController object and then try to connect with the server. Constructs a buffer,
     * ClientSender and a ClientReceiver Object. Then starts two Threads.
     * @param clientController The received ClientController object.
     */
    public ClientCommunicationController(ClientController clientController) {
        this.clientController = clientController;
        connect();
        userBuffer = new Buffer<>();
        activityBuffer = new Buffer<>();
        ClientSender clientSender = new ClientSender();
        clientSender.start();
        new ClientReceiver().start();
    }

    /**
     * Tries to create a new socket and connect to the server's IP.
     */
    public void connect() {
        try {
            socket = new Socket("127.0.0.1", 4343);
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
            ois = null;
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
                    oos.writeObject(user);

                    if(user.getUserType() == UserType.LOGOUT){
                        System.out.println(className + " user is logging out");
                        disconnect();
                    }

                    Activity activity = activityBuffer.get();
                    oos.writeObject(activity);

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
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            while (ois != null) { //TODO: Går ej att logga ut och stänga strömmen. Se över villkor!
                try {
                    sleep(500);
                    Object object = ois.readObject();

                    if (object instanceof User) {
                        User user = (User) object;
                        UserType userType = user.getUserType();

                        switch (userType) {
                            case SENDUSER:
                                clientController.receiveExistingUser(user);

                                break;

                            case SENDWELCOME:
                                clientController.receiveAcceptedUser(user);

                                break;
                        }
                    }
                    else if (object instanceof Activity) {
                        Activity activity = (Activity) object;
                        clientController.receiveNotificationFromCCC(activity);
                    }
                    else System.out.println("Den gick inte in i någon if-sats :(");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
