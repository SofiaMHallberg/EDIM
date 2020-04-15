package server;

import java.io.Serializable;

public class Activity implements Serializable {
    private String activityName;
    private String activityInstruction;
    private String activityInfo;

    private boolean isCompleted = false;


    private String toUser;

    public Activity () {}
    public Activity(String activityName) {
        this.activityName=activityName;
    }

    public String getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(String activityInfo) {
        this.activityInfo = activityInfo;
    }

    public void setActivityName(String activityName) {
        this.activityName=activityName;
    }
    public String getActivityName() {
        return activityName;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
    public String getActivityInstruction() {
        return activityInstruction;
    }

    public void setActivityInstruction(String activityInstruction) {
        this.activityInstruction = activityInstruction;
    }
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

