package server;

import java.io.*;
import java.util.LinkedList;

/**
 * This class creates a register that handles Activity objects.
 *
 * @author Carolin Nordstrom, Oscar Kareld, Chanon Borgstrom, Sofia Hallberg.
 * @version 1.0
 */

public class ActivityRegister {
    private LinkedList<Activity> activityRegister;
    private String className="Class: ActivityRegister ";

    public ActivityRegister(String file) {
        createRegister(file);
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
                activity.createActivityImage(br.readLine());
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
