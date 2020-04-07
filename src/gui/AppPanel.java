package gui;

import server.Activity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class AppPanel extends JPanel {
    private MainPanel mainPanel;

    private JLabel lblUserInfo;

    private LinkedList<Activity> activities;
    private JList activityList;

    private JButton btnLogOut;

    private BorderLayout borderLayout = new BorderLayout();
    private ActionListener listener = new ButtonListener();
    private DefaultListModel listModel;

    private String className = "AppPanel: ";

    public AppPanel(MainPanel mainPanel, String userName) {
        this.mainPanel = mainPanel;
        setupPanel();
        createComponents(userName);
        activities = new LinkedList<>();
    }

    public void setupPanel() {
        setSize(new Dimension(819, 438));
    }

    public void createComponents(String userName) {
        setLayout(borderLayout);

        createActivityList();

        btnLogOut = new JButton("Logga ut");
        lblUserInfo = new JLabel("Hej " + userName);

        add(lblUserInfo, BorderLayout.NORTH);
        add(activityList, BorderLayout.CENTER);
        add(btnLogOut, BorderLayout.SOUTH);

        btnLogOut.addActionListener(listener);
        addActivityListener();
    }

    public void createActivityList() {
        listModel = new DefaultListModel();
        activityList = new JList<>(listModel);
        activityList.setPreferredSize(new Dimension(400, 320));
        activityList.setBorder(BorderFactory.createTitledBorder("Avklarade aktiviteter!"));
        activityList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        Font font = new Font("Courier New", Font.PLAIN, 14);
        activityList.setFont(font);
    }

    public void addActivityListener() {
        activityList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String activityName = (String) activityList.getSelectedValue();
                for (Activity activity : activities) {
                    if (activity.getActivityName().equals(activityName)) {
                        showActivityInfo(activity.getActivityInfo());
                    }
                }
            }
        });
    }

    public void updateActivityList(Activity activity) {
        activities.add(activity);
        listModel.addElement(activity.getActivityName());
//        System.out.println(className +"updateActivityList: "+ lblActivity.getText());
        updateUI();
    }

    public void showActivityInfo(String activityInfo) {
        JOptionPane.showMessageDialog(null, activityInfo);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object click = e.getSource();
            if (click == btnLogOut) {
                mainPanel.logOut();
            }
        }
    }
}
