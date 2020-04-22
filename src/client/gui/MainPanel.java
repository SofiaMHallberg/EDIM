package client.gui;

import server.Activity;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private AppPanel appPanel;
    private String userName;

    public MainPanel(MainFrame mainFrame, String userName) {
        this.mainFrame = mainFrame;
        this.userName = userName;
        setupPanel();
        appPanel = new AppPanel(this, userName);
        showAppPanel();
    }

    public void setupPanel() {
        setSize(new Dimension(819, 438));
        setBackground(Color.YELLOW);
        setBorder(BorderFactory.createTitledBorder("Välkommen, " + userName));
    }

    public void showAppPanel() {
        add(appPanel);

        //TODO: De undre raderna ska komma ifrån ClientController.
        /*
        Activity activity = new Activity("Squats");
        activity.setActivityInfo("Den här övningen ökar din koncentration med 5%");
        activity.setActivityInstruction("Gör 10 squats");
        appPanel.updateActivityList(activity);

         */
    }

    public AppPanel getAppPanel() {
        return appPanel;
    }

    public void logOut() {
        mainFrame.logOut();
    }

    public void sendActivityFromGUI(Activity activity) {
        mainFrame.sendActivityFromGUI(activity);
    }

    public void sendChosenInterval(int interval) {
        mainFrame.sendChosenInterval(interval);
    }
}
