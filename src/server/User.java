package server;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {
    private static final long serialVersionUID = 42L; //489241266336029083L;//-6356381908430432467L;
    private LinkedList<Activity> completedActivities;
    private String userName;
    private int age;
    private int notificationInterval = 45;
    private boolean isOnline;
    private UserType userType;
    private Activity delayedActivity;
    private String className="Class: User ";

    public User(String userName) {
        this.userName=userName;
        completedActivities=new LinkedList<>();
    }

    public LinkedList<Activity> getCompletedActivities() {
        return completedActivities;
    }

    public void addActivityToList(Activity activity) {
        completedActivities.add(activity);
    }

    public void setCompletedActivities(LinkedList<Activity> completedActivities) {
        this.completedActivities = completedActivities;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNotificationInterval() {
        return notificationInterval;
    }

    public void setNotificationInterval(int notificationInterval) {
        this.notificationInterval = notificationInterval;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Activity getDelayedActivity() {
        return delayedActivity;
    }

    public void setDelayedActivity(Activity delayedActivity) {
        this.delayedActivity = delayedActivity;
    }
}
