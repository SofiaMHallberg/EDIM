package client.gui;

import server.Activity;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class AppPanel extends JPanel {
    private MainPanel mainPanel;

    private String[] interval;
    private JLabel lblUserInfo;
    private JTextArea taActivityInfo;
    private JComboBox cmbTimeLimit;

    private LinkedList<Activity> activities;
    private JList activityList;

    private JButton btnLogOut;
    private JButton btnInterval;

    private JPanel intervalPnl;

    private BorderLayout borderLayout = new BorderLayout();
    private ActionListener listener = new ButtonListener();
    private DefaultListModel listModel;

    private String className = "Class: AppPanel: ";
    private ImageIcon activityIcon;
    private Color clrPanels = new Color(142, 166, 192);
    private Color clrMidPanel = new Color(127, 140, 151, 151);




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
        createTAActivityInfo();
        createCBTimeLimit();
        createIntervalPanel();

        btnLogOut = new JButton("Logga ut");

        add(activityList, BorderLayout.CENTER);
        add(btnLogOut, BorderLayout.SOUTH);
        add(taActivityInfo, BorderLayout.EAST);
        add(intervalPnl, BorderLayout.WEST);

        btnLogOut.addActionListener(listener);
        btnInterval.addActionListener(listener);
        addActivityListener();
        createActivityIcon();
    }

    public void createIntervalPanel() {
        intervalPnl = new JPanel();
        intervalPnl.setBackground(clrPanels); //TODO Färg
        intervalPnl.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
        btnInterval = new JButton("Ändra intervall");

        intervalPnl.add(cmbTimeLimit, BorderLayout.CENTER);
        intervalPnl.add(btnInterval, BorderLayout.SOUTH);
    }

    public void createCBTimeLimit() {
        interval = new String[]{"15", "30", "45", "60"};
        cmbTimeLimit = new JComboBox<>(interval);
        cmbTimeLimit.setSelectedIndex(2);
    }

    public void createTAActivityInfo() {
        taActivityInfo = new JTextArea();
        taActivityInfo.setBackground(clrPanels); //TODO Färg
        taActivityInfo.setPreferredSize(new Dimension(200, 80));
        taActivityInfo.setLineWrap(true);
        taActivityInfo.setWrapStyleWord(true);
        Font font = new Font("SansSerif", Font.PLAIN, 14); //Sarseriff
        taActivityInfo.setFont(font);
        taActivityInfo.setEditable(false);
    }

    public void createActivityList() {
        listModel = new DefaultListModel();
        activityList = new JList<>(listModel);
        activityList.setPreferredSize(new Dimension(400, 320));
        activityList.setBorder(BorderFactory.createTitledBorder("Avklarade aktiviteter"));
        activityList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        //activityList.setBackground(clrMidPanel); //TODO Färg
        Font font = new Font("SansSerif", Font.PLAIN, 14);
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
        //JOptionPane.showMessageDialog(null, activityInfo);
        taActivityInfo.setText(activityInfo);
    }

    public void createActivityIcon() {
        activityIcon = new ImageIcon("images/exercise.png");
        Image image = activityIcon.getImage();
        Image newImg = image.getScaledInstance(50,50, Image.SCALE_SMOOTH);
        activityIcon = new ImageIcon(newImg);
    }

    public void showNotification(Activity activity) {
        String[] buttons = {"Jag har gjort aktiviteten!", "Påminn mig om fem minuter",};

        int answer = JOptionPane.showOptionDialog(null, activity.getActivityInstruction(), "Ny aktivitet",
                JOptionPane.WARNING_MESSAGE, 0, activityIcon, buttons, buttons[0]);
        if (answer == 0) {
            activity.setCompleted(true);
            mainPanel.sendActivityFromGUI(activity);
            updateActivityList(activity);

        } else { //if (answer == 1)
            activity.setCompleted(false);
            mainPanel.sendActivityFromGUI(activity);
        }
    }

    public void showWelcomeMessage(String userName) {
        JOptionPane.showMessageDialog(null, "Välkommen " + userName + "\nNu ska vi röra på oss!");
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object click = e.getSource();
            int interval;
            if (click == btnLogOut) {
                mainPanel.logOut();
            }
            if (click == btnInterval) {
                interval = Integer.parseInt((String) cmbTimeLimit.getSelectedItem());
                mainPanel.sendChosenInterval(interval);
            }
        }
    }

}
