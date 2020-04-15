package server;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

public class ActivityRegister {
    private LinkedList<Activity> activityRegister;
    private String className="server.ActivityRegister ";

    public ActivityRegister(String file) {
        createRegister(file);
        System.out.println(className+activityRegister.get(1).getActivityName());
    }

    private void createRegister(String file) {
        activityRegister=new LinkedList<Activity>();
        int nbrOfActivities;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            nbrOfActivities=Integer.parseInt(br.readLine());
            for (int i = 0; i < nbrOfActivities; i++) {
                Activity activity=new Activity();
                activity.setActivityName(br.readLine());
                activity.setActivityInstruction(br.readLine());
                activity.setActivityInfo(br.readLine());
                activityRegister.add(activity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Activity> getActivityRegister() {
        return activityRegister;
    }
}
