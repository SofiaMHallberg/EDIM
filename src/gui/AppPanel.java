package gui;

import server.Activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppPanel extends JPanel {
    private MainPanel mainPanel;

    private JLabel lblUserInfo;

    private JList<Activity> activityList;

    private JButton btnLogOut;

    private BorderLayout borderLayout = new BorderLayout();
    private ActionListener listener = new ButtonListener();

    public AppPanel(MainPanel mainPanel, String userName) {
        this.mainPanel = mainPanel;
        setupPanel();
        createComponents(userName);
    }

    public void setupPanel() {
        setSize(new Dimension(819, 438));
    }

    private void createComponents(String userName) {
        setLayout(borderLayout);

        btnLogOut = new JButton("Logga ut");
        activityList = new JList<>();
        activityList.setPreferredSize(new Dimension(400,320));

        lblUserInfo = new JLabel("Hej " + userName);
        add(lblUserInfo, BorderLayout.NORTH);
        add(activityList,BorderLayout.CENTER);
        add(btnLogOut, BorderLayout.SOUTH);

        btnLogOut.addActionListener(listener);
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object click = e.getSource();
            if (click == btnLogOut) {
                mainPanel.logOut();
            }
        }
    }
}
