package gui;

import javax.swing.*;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private LogInPanel logInPanel;
    private AppPanel appPanel;

    public MainPanel(MainFrame mainFrame, String userName) {
        this.mainFrame = mainFrame;
        appPanel = new AppPanel(this, userName);

    }

    public void showAppPanel() {
        add(appPanel);
    }
}
