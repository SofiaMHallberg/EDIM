package server;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * This class handles the information about an Activity object.
 *
 * @author Carolin Nordström & Oscar Kareld & Chanon Borgström & Sofia Hallberg.
 * @version 1.0
 */

public class Activity implements Serializable {
    private static final long serialVersionUID = 200428L;
    private String activityName;
    private String activityInstruction;
    private String activityInfo;
    private boolean isCompleted = false;
    private String activityUser;
    private ImageIcon activityImage;
    private Date date=new Date();
    private long time;




    public Activity () {}

    public String getTime() {
        String dateTime=date.toString();
        return dateTime;
    }

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

    public String getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(String activityUser) {
        this.activityUser = activityUser;
    }

    public void setActivityImage(ImageIcon icon) {
        activityImage = icon;
    }
    public ImageIcon getActivityImage() {
        return activityImage;
    }
    public void createActivityImage(String fileName) {
        activityImage = new ImageIcon(fileName);
    }
}


