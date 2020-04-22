package server;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserTimer implements ActionListener {

    private Timer timer;
    private int currentTime;
    private int timeInterval;
    private ServerController serverController;
    private User user;

    private String className = "Class: UserTimer ";

    public UserTimer(ServerController serverController, User user) {
        currentTime = 0;
        timeInterval = 45;
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

    public void setTimeInterval(int timeInterval){
        this.timeInterval = timeInterval;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentTime++;
        if (checkTimeInterval()) {
            serverController.sendActivity(user.getUserName());
            stopTimer();
            startTimer();
        }
        System.out.println(className + currentTime);
    }


    public boolean checkTimeInterval() {
        if (user.getNotificationInterval() == timeInterval) {
            return true;
        }
        return false;

    }
}
