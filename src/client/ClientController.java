package client;

import server.Activity;


import gui.MainFrame;
import server.User;
import server.UserType;

import javax.swing.*;

/**
 * This class manages the logic for the Client and controls the data flow.
 *
 * @version 1.0
 * @autor Carolin Nordström & Oscar Kareld.
 */

public class ClientController {

    private MainFrame mainFrame;
    private ClientCommunicationController ccc;
    private User user;
    private String className = "ClientController: ";

    /**
     * Constructs a MainFrame and a ClientCommunicationController object. Then calls the method createUser.
     */
    public ClientController() {
        mainFrame = new MainFrame(this);
    }

    /**
     * Receives a String and creates a new User object and calls the logIn method.
     *
     * @param userName
     */
    public void createUser(String userName) {
        user = new User(userName);
        logIn();
    }

    /**
     * Receives an Activity Object and sends it forth to the ClientCommunicationController.
     *
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
        ccc = new ClientCommunicationController(this);
        ccc.sendUser(user);
    }

    /**
     *
     */
    public void logOut() {
        user.setUserType(UserType.LOGOUT);
        ccc.sendUser(user);
    }

    /**
     * Receives an Activity object an sends it forth to MainFrame.
     *
     * @param activity the received object.
     */
    public void receiveNotificationFromCCC(Activity activity) {
        mainFrame.showNotification(activity);
    }

    public void receiveCompletedActivity(Activity activity) {

    }

    public void receiveExistingUser(User user) {
        System.out.println(className+ "receiveExisting");
        this.user = user;
    }

    public void receiveAcceptedUser(User userAccepted) {
        this.user = userAccepted;
        mainFrame.sendWelcomeMessage(userAccepted.getUserName());
    }

    public void setInterval(int interval){
        user.setNotificationInterval(interval);
        user.setUserType(UserType.SENDINTERVAL);
        System.out.println(className + user.getUserName() +user.getNotificationInterval());
        ccc.sendUser(user);
    }
}
