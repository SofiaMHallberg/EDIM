package server;

import java.io.Serializable;

public class Activity implements Serializable {
    private String activityName;
    private String activityInfo;
    private String toUser;

    public Activity(String activityName) {
        this.activityName=activityName;
    }

    public String getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(String activityInfo) {
        this.activityInfo = activityInfo;
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
}

