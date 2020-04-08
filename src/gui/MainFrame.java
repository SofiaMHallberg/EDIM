package gui;

import client.ClientController;
import server.Activity;

import javax.swing.*;

/**
 * This class starts GUI window and awaits the users input for a user name.
 *
 * @version 1.0
 * @autor Carolin Nordström & Oscar Kareld.
 */

public class MainFrame extends JFrame {
    private ClientController clientController;
    private MainPanel mainPanel;
    private AppPanel appPanel;
    private NotificationPanel notificationPanel;
    private String userName;
    private LogInFrame logInFrame;



    /**
     * Receives a clientController object and opens call for the method which opens a GUI window.
     *
     * @param clientController The received ClientController object.
     */
    public MainFrame(ClientController clientController) {
        this.clientController = clientController;
       // setupFrame();
        createLoginFrame();
    }

    public void createLoginFrame() {
        logInFrame = new LogInFrame(this);
    }

    public void setupFrame() {
        setBounds(0, 0, 819, 438);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("EDIM");
        setResizable(true);            // Prevent user from change size
        setLocationRelativeTo(null);    // Start middle screen
        setVisible(true);

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void showAppPanel(String userName) {
        clientController.createUser(userName);
        this.userName = userName;
        setupFrame();
        mainPanel = new MainPanel(this, userName);
        setContentPane(mainPanel);


    }

    public void logOut() {
        clientController.logOut();
        dispose();
    }
/*
    public static void main(String[] args) {
        LogInFrame logInFrame = new LogInFrame();
    }

 */


    public void showNotification(Activity activity) {
        appPanel.showNotification(activity);
    }


    public void sendActivityFromGUI(Activity activity) {
        clientController.sendActivityToCCC(activity);
    }
}
