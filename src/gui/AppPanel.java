package gui;

import javax.swing.*;

public class AppPanel extends JPanel {
    private MainPanel mainPanel;
    private JLabel lblUserInfo;

    public AppPanel(MainPanel mainPanel, String userName) {
        this.mainPanel = mainPanel;
        createComponents(userName);
    }

    private void createComponents(String userName) {
        lblUserInfo = new JLabel("Hej " + userName);
        add(lblUserInfo);

    }
}
