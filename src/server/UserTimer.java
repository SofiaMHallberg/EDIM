package server;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class handles the information for a user's timer.
 *
 * @version 1.0
 * @author Carolin Nordström & Oscar Kareld & Chanon Borgström & Sofia Hallberg.
 */

public class UserTimer implements ActionListener {

    private Timer timer;
    private int currentTime;
    private int delay;
    private int userInterval;
    private boolean activateDelay;
    private ServerController serverController;
    private volatile User user;
    private String className = "Class: UserTimer ";


    public UserTimer(ServerController serverController, User user) {
        currentTime = 0;
        this.serverController = serverController;
        this.user = user;
    }

    /**
     * Starts the timer object.
     */
    public void startTimer() {
        timer = new Timer(1000, this);
        timer.start();
    }

    /**
     * Stops the timer object.
     */
    public void stopTimer() {
        currentTime = 0;
        if(timer!=null) {
            timer.stop();
            timer = null;
        }
    }

    /**
     * Updates the user object with the received user.
     * @param user the received user.
     */
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
                activateDelay = false;
            }
        }
//        System.out.println(className + currentTime);
    }

    /**
     * Calls for {@link ServerController} method sendActivity.
     */
    public void sendActivity() {
        serverController.sendActivity(user.getUsername());
    }

    /**
     * Checks if the delayed interval is as much as the current time.
     * @return a boolean if it's true or false.
     */
    public boolean checkDelayInterval() {
        if (currentTime >= delay) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the currentTime is at the same as the User's notification interval.
     * @return a boolean if they are the same value.
     */
    public boolean checkTimeInterval() {
        if (currentTime >= user.getNotificationInterval()) {
            return true;
        }
        return false;

    }
}
