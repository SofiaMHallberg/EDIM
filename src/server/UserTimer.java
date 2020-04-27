package server;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserTimer implements ActionListener {

    private Timer timer;
    private int currentTime;
    private ServerController serverController;
    private User user;
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

    public void updateUser(User user){
        this.user = user;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentTime++;
        System.out.println(className + checkTimeInterval());
        if (checkTimeInterval()) {
            serverController.sendActivity(user.getUserName());
            stopTimer();
        }
        System.out.println(className + currentTime);
    }


    public boolean checkTimeInterval() {
        if (user.getNotificationInterval() >= currentTime) {
            return true;
        }
        return false;

    }
}
