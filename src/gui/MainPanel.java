package gui;

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
    }

    public void logOut() {
        mainFrame.logOut();
    }
}
