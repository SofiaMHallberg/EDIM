package client;

import server.Activity;


import gui.MainFrame;
import server.User;
import server.UserType;

import javax.swing.*;

public class ClientController {

    private MainFrame mainFrame;
    private ClientCommunicationController ccc;
    private User user;

    public ClientController() {
        mainFrame = new MainFrame(this);
        ccc = new ClientCommunicationController(this);
        createUser(mainFrame.getUserName());
    }

    public void createUser(String userName) {
        user = new User(userName);
        logIn();
    }


    public void sendCompletedActivity(Activity activity) {
        ccc.sendActivity(activity);
    }

    public void logIn() {
        user.setUserType(UserType.LOGIN);
        ccc.sendUser(user);
    }

    public void logOut(User user) {
        //   this.user.setOnlineStatus(false);

    }

    public void receiveNotificationFromCCC(Activity activity) {
        JOptionPane.showMessageDialog(null, "Nu är det dags att göra en aktivitet! \nDu ska göra: "
                + activity.getActivityName() + "\nInformation: " + activity.getActivityInfo());
    }

    public void receiveCompletedActivity(Activity activity) {

    }

    public void receiveExistingUser(User user) {
        System.out.println("CC, receiveExisting");
        this.user = user;
        JOptionPane.showMessageDialog(null, "Välkommen tillbaka, " + user.getUserName());
        System.out.println("Slut på Existing-metoden");
    }

    public void receiveAcceptedUser(String userAccepted) {
        JOptionPane.showMessageDialog(null, userAccepted);
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
