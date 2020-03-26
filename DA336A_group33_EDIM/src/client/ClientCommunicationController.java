package client;

import server.Activity;
import server.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientCommunicationController {
    private ClientController clientController;
    private Buffer<User> userBuffer;
    private Buffer<Activity> activityBuffer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    private int port;
    private String ip, className = "Class: ClientCommunicationController, Method: ";

    public ClientCommunicationController(ClientController clientController) {
        this.clientController = clientController;
        connect();
        userBuffer = new Buffer<>();
        ClientSender clientSender = new ClientSender();
        clientSender.start();
        new ClientReceiver().start();
    }

    public void connect() {
        try {
            socket = new Socket("127.0.0.1", 3343);
            System.out.println(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUser(User user) {
        userBuffer.put(user);
    }
    public void sendActivity(Activity activity) {
        activityBuffer.put(activity);
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientSender extends Thread {

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
