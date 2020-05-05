package server;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserTimer implements ActionListener {

    private Timer timer;
    private int currentTime;
    private int delay;
    private boolean activateDelay;
    private ServerController serverController;
    private volatile User user;
    private String className = "Class: UserTimer ";


    public UserTimer(ServerController serverController, User user) {
        currentTime = 0;
        this.serverController = serverController;
        this.user = user;
    }

    public void startTimer() {
        timer = new Timer(1000, this);
        timer.start();
    }

    public void stopTimer() {
        currentTime = 0;
        timer.stop();
        timer = null;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public User getUser() {
        return user;
    }

    public void setDelayTimer(int delay) {
        this.delay = delay;
    }

    public void setActiveDelay(boolean active){
        this.activateDelay = active;
    }

    public void actionPerformed(ActionEvent e) {
        currentTime++;
        if (checkTimeInterval()) {
            sendActivity();
            stopTimer();
        } else if (activateDelay) {
            if (checkDelayInterval()) {
                sendActivity();
                stopTimer();
            }
        }
//        System.out.println(className + currentTime);
    }

    public void sendActivity() {
        serverController.sendActivity(user.getUserName());
    }

    public boolean checkDelayInterval() {
        if (currentTime >= delay) {
            return true;
        }
        return false;
    }


    public boolean checkTimeInterval() {
        if (currentTime >= user.getNotificationInterval()) {
            return true;
        }
        return false;

    }
}
