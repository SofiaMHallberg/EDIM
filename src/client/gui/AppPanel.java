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

/**
 * This is the panel in the frame that contains pretty much all of the components in the GUI.
 *
 * @version 1.0
 * @author Oscar Kareld, Chanon Borgstrom, Carolin Nordstrom
 */
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


    public AppPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setupPanel();
        createComponents();
        activities = new LinkedList<>();
    }

    public void setupPanel() {
        setSize(new Dimension(819, 438));
    }

    public void createComponents() {
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
        startTimer(Integer.parseInt((String) cmbTimeLimit.getSelectedItem()), 59);
        centerPnl.add(cmbTimeLimit);
        centerPnl.add(btnInterval);
        intervalPnl.add(lblInterval, BorderLayout.NORTH);
        intervalPnl.add(centerPnl, BorderLayout.CENTER);
        intervalPnl.add(lblTimerInfo, BorderLayout.SOUTH);
    }

    public void updateLblInterval() {
        int interval;
        interval = Integer.parseInt((String) cmbTimeLimit.getSelectedItem());
        lblInterval.setText("Aktivt tidsintervall: " + interval + " minuter");
    }

    public void createCBTimeLimit() {
        interval = new String[]{"5", "15", "30", "45", "60"};
        cmbTimeLimit = new JComboBox<>(interval);
        cmbTimeLimit.setSelectedIndex(3);
    }

    public void startTimer(int minutes, int seconds) {
        minuteInterval = minutes - 1;
        secondInterval = seconds;
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                String time;
                if (secondInterval < 10) {
                    time = String.format("timer: %d:0%d", minuteInterval, secondInterval);
                } else {
                    time = String.format("timer: %d:%d", minuteInterval, secondInterval);
                }
                lblTimerInfo.setText(time);
                decreaseInterval();
            }
        }, delay, period);
    }

    public void decreaseInterval() {
        secondInterval --;
        if (secondInterval == -1) {
            minuteInterval--;
            if (minuteInterval == -1) {
                stopTimer();
            }
            secondInterval = 59;
        }
    }

    public void countTimerInterval(int chosenInterval) {
        int difference = 0;
        if (minuteInterval > chosenInterval) { //Vi var på 15 (minuteInterval), sedan ändrade vi till 5 (chosenInterval)
            difference = minuteInterval - chosenInterval;
            System.out.println("if-satsen: Difference: " + difference);
            minuteInterval = minuteInterval - difference - 1; //-1
            System.out.println("minuteInterval: " + minuteInterval);
        } else {
            difference = chosenInterval - minuteInterval;
            System.out.println("Else-satsen: Difference: " + difference);
            minuteInterval = minuteInterval + difference - 1;
            System.out.println("minuteInterval: " + minuteInterval);
        }
    }

    public void stopTimer() {
        timer.cancel();
    }

    public void createTAActivityInfo() {
        taActivityInfo = new JTextArea();
        taActivityInfo.setBackground(clrPanels);
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
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        activityList.setFont(font);
    }

    public void addActivityListener() {
        activityList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String activityName = (String) activityList.getSelectedValue();
                String newActivityName = splitActivityNameAndTime(activityName);
                for (Activity activity : activities) {
                    if (activity.getActivityName().equals(newActivityName)) {
                        showActivityInfo(activity.getActivityInfo());
                    }
                }
            }
        });
    }

    public String splitActivityNameAndTime(String activityName) {
        activityName = activityName.replaceAll("[0-9]", "");
        activityName = activityName.replaceAll(":", "");
        activityName = activityName.replaceAll(" ", "");
        return activityName;
    }

    public void updateActivityList(Activity activity) {
        stopTimer();
        startTimer(Integer.parseInt((String) cmbTimeLimit.getSelectedItem()), 59);
        activities.add(activity);
        listModel.addElement(activity.getActivityName() + " " + activity.getTime());
        String newActivityName = splitActivityNameAndTime(activity.getActivityName());
        activity.setActivityName(newActivityName);
        updateUI();
    }

    public void showActivityInfo(String activityInfo) {
        taActivityInfo.setText(activityInfo);
    }

    public ImageIcon createActivityIcon(Activity activity) {
        ImageIcon activityIcon = activity.getActivityImage();
        Image image = activityIcon.getImage();
        Image newImg = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public void showNotification(Activity activity) {
        Toolkit.getDefaultToolkit().beep();
        ImageIcon activityIcon = createActivityIcon(activity);
        String[] buttons = {"Jag har gjort aktiviteten!", "Påminn mig om fem minuter",};
        String instruction = activity.getActivityInstruction();
        String[] instructions = new String[3];

        if (instruction.contains("&")) {
            instructions = instruction.split("&");
        }
        int answer = welcomePane.showOptionDialog(null, instructions, activity.getActivityName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, activityIcon, buttons, buttons[0]);
        if (answer == 0) {
            activity.setCompleted(true);
            mainPanel.sendActivityFromGUI(activity);
            updateActivityList(activity);

        } else {
            stopTimer();
            startTimer(5, 59);
            activity.setCompleted(false);
            mainPanel.sendActivityFromGUI(activity);
        }
    }

    public class welcomePane extends JOptionPane {
        @Override
        public int getMaxCharactersPerLineCount() {
            return 10;
        }
    }

    public void showWelcomeMessage(String userName) {
        ImageIcon welcomeIcon = new ImageIcon("imagesClient/exercise.png");
        Image image = welcomeIcon.getImage();
        Image newImg = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JOptionPane.showMessageDialog(null, "Välkommen " + userName + "!" + "\nEDIM kommer skicka notiser till dig med jämna mellanrum,\n" +
                "med en fysisk aktivitet som ska utföras.\n" +
                "Hur ofta du vill ha dessa notiser kan du ställa in själv.", "Välkommen till Edim ", 2, new ImageIcon(newImg));
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
                countTimerInterval(interval);
                mainPanel.sendChosenInterval(interval);
                updateLblInterval();
            }
        }
    }
}
