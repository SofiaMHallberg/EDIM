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
import java.util.Timer;
import java.util.TimerTask;

public class AppPanel extends JPanel {
    private MainPanel mainPanel;

    private String[] interval;
    private JLabel lblTimerInfo;
    private JTextArea taActivityInfo;
    private JComboBox cmbTimeLimit;
    private LinkedList<Activity> activities;
    private JList activityList;

    private JButton btnLogOut;
    private JButton btnInterval;
    private JPanel intervalPnl;
    private JLabel lblInterval;

    private BorderLayout borderLayout = new BorderLayout();
    private ActionListener listener = new ButtonListener();
    private DefaultListModel listModel;

    private String className = "Class: AppPanel: ";
    private Color clrPanels = new Color(142, 166, 192);
    private Color clrMidPanel = new Color(127, 140, 151, 151);

    private Timer timer;
    private int minuteInterval;
    private int secondInterval;
    private int timerInterval;


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
//        createActivityIcon();
    }

    public void createIntervalPanel() {
        intervalPnl = new JPanel();
        intervalPnl.setLayout(new BorderLayout());
        intervalPnl.setBackground(clrPanels);
        intervalPnl.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY));

        lblInterval = new JLabel();
        lblTimerInfo = new JLabel();
        JPanel centerPnl = new JPanel();
        centerPnl.setSize(new Dimension(intervalPnl.getWidth(), intervalPnl.getHeight()));
        centerPnl.setBackground(clrPanels);
        updateLblInterval();
        btnInterval = new JButton("Ändra intervall");
        startTimer( Integer.parseInt((String) cmbTimeLimit.getSelectedItem()),60);
        centerPnl.add(cmbTimeLimit);
        centerPnl.add(btnInterval);
//        intervalPnl.add(cmbTimeLimit, BorderLayout.CENTER);
//        intervalPnl.add(btnInterval, BorderLayout.EAST);
        intervalPnl.add(lblInterval, BorderLayout.NORTH);
        intervalPnl.add(centerPnl, BorderLayout.CENTER);
    }
    public void updateLblInterval() {
        int interval;
        interval = Integer.parseInt((String) cmbTimeLimit.getSelectedItem());
        lblInterval.setText("Aktivt tidsintervall: " + interval + " minuter");
    }

    public void createCBTimeLimit() {
        interval = new String[]{"5","15", "30", "45", "60"};
        cmbTimeLimit = new JComboBox<>(interval);
        cmbTimeLimit.setSelectedIndex(1);
    }

    public void startTimer(int minutes, int seconds) {
        minuteInterval = minutes;
        secondInterval = seconds;
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                String time = String.format("timer: %d:%d", minuteInterval, secondInterval);
                lblTimerInfo.setText(time);
                System.out.println(time);
                decreaseInterval();
            }
        }, delay, period);
    }

    public void decreaseInterval() {
        secondInterval --;
        if (secondInterval == 0) {
            minuteInterval--;
            if (minuteInterval == 0) {
                stopTimer();
            }
            secondInterval = 59;
        }
        updateUI();
    }

    public void stopTimer() {
        timer.cancel();
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
        listModel.addElement(activity.getActivityName()+" "+activity.getTime());
//        System.out.println(className +"updateActivityList: "+ lblActivity.getText());
        updateUI();
    }

    public void showActivityInfo(String activityInfo) {
        //JOptionPane.showMessageDialog(null, activityInfo);
        taActivityInfo.setText(activityInfo);
    }

    public ImageIcon createActivityIcon(Activity activity) {
        ImageIcon activityIcon = activity.getActivityImage();
        Image image = activityIcon.getImage();
        Image newImg = image.getScaledInstance(150,150, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public void showNotification(Activity activity) {
        ImageIcon activityIcon = createActivityIcon(activity);
        String[] buttons = {"Jag har gjort aktiviteten!", "Påminn mig om fem minuter",};
        String instruction = activity.getActivityInstruction();
        String[] instructions = new String[3];

        if(instruction.contains("&")) {
            instructions = instruction.split("&");
        }

        int answer = MyJOptionPane.showOptionDialog(null, instructions, activity.getActivityName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, activityIcon, buttons, buttons[0]);

        if (answer == 0) {
            activity.setCompleted(true);
            mainPanel.sendActivityFromGUI(activity);
            updateActivityList(activity);

        } else { //if (answer == 1)
            activity.setCompleted(false);
            mainPanel.sendActivityFromGUI(activity);
        }
    }

    public class MyJOptionPane extends JOptionPane {
        @Override
        public int getMaxCharactersPerLineCount() {
            return 10;
        }
    }

    public void showWelcomeMessage(String userName) {
        JOptionPane.showMessageDialog(null, "Välkommen " + userName + ".\nEDIM kommer skicka notiser till dig med jämna mellanrum,\n" +
                "med en fysisk aktivitet som ska utföras.\n" +
                "Hur ofta du vill ha dessa notiser kan du ställa in själv.");
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
                updateLblInterval();
            }
        }
    }

}
