package client;

import server.Activity;


import gui.MainFrame;
import server.User;
import server.UserType;

import javax.swing.*;

/**
 * This class manages the logic for the Client and controls the data flow.
 * @autor Carolin Nordström & Oscar Kareld.
 * @version 1.0
 */

public class ClientController {

    private MainFrame mainFrame;
    private ClientCommunicationController ccc;
    private User user;

    /**
     * Constructs a MainFrame and a ClientCommunicationController object. Then calls the method createUser.
     */
    public ClientController() {
        mainFrame = new MainFrame(this);
        ccc = new ClientCommunicationController(this);
        createUser(mainFrame.getUserName());
    }

    /**
     * Receives a String and creates a new User object and calls the logIn method.
     * @param userName
     */
    public void createUser(String userName) {
        user = new User(userName);
        logIn();
    }

    /**
     * Receives an Activity Object and sends it forth to the ClientCommunicationController.
     * @param activity the received object.
     */
    public void sendActivityToCCC(Activity activity) {
        ccc.sendActivity(activity);
    }

    /**
     * Sets the UserType to LOGIN and sends the it to ClientCommunicationController.
     */
    public void logIn() {
        user.setUserType(UserType.LOGIN);
        ccc.sendUser(user);
    }

    /**
     *
     */
    public void logOut() {
        //   this.user.setOnlineStatus(false);

    }

    /**
     * Receives an Activity object an sends it forth to MainFrame.
     * @param activity the received object.
     */
    public void receiveNotificationFromCCC(Activity activity) {
        mainFrame.showNotification(activity);
    }

    public void receiveCompletedActivity(Activity activity) {

    }

    public void receiveExistingUser(User user) {
        System.out.println("CC, receiveExisting");
        this.user = user;

        JOptionPane.showMessageDialog(null, "Välkommen tillbaka, " + user.getUserName());
        System.out.println("Slut på Existing-metoden");
    }

    public void receiveAcceptedUser(User userAccepted) {
        JOptionPane.showMessageDialog(null, "Välkommen " + userAccepted.getUserName());
        //TODO: Detta ska göras i MainFrame.
    }
/*
    public static void main(String[] args) {
        ClientController cc = new ClientController();
        Activity act = new Activity();
        String str = "Välkommen!";
        User u = new User("Oscar");

        cc.receiveExistingUser(u);
        cc.receiveNotificationFromCCC(act);
        cc.receiveAcceptedUser(str);
    }

 */
}
