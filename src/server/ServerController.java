package server;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

/**
 * This class handles the logic of the in and out coming object.
 *
 * @author Sofia Hallberg & Chanon Borgström.
 * @version 1.0
 */

public class ServerController extends Thread {
    private Buffer<User> onLineBuffer;
    private Buffer<User> sendUserBuffer;
    private Buffer<Activity> sendNewActivityBuffer;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private HashMap<String, UserTimer> userTimerHashMap;
    private ConnectionServer connectionServer;
    private CommunicationServer communicationServer;
    private UserRegister userRegister;
    private ActivityRegister activityRegister;
    private String className = "Class: ServerController ";

    /**
     * Constructs all the buffers and servers and HashMaps that is needed.
     *
     * @param port the received port number.
     */
    public ServerController(int port) {
        onLineBuffer = new Buffer();
        socketHashMap = new HashMap();
        sendUserBuffer = new Buffer();
        sendNewActivityBuffer = new Buffer();
        connectionServer = new ConnectionServer(port, socketHashMap, onLineBuffer);
        communicationServer = new CommunicationServer(socketHashMap, sendUserBuffer, sendNewActivityBuffer);
        userRegister = new UserRegister();
        readContacts("files/users.txt");
        activityRegister = new ActivityRegister("files/activities.txt");
        System.out.println(className + activityRegister.getActivityRegister().size());
        System.out.println(className + activityRegister.getActivityRegister().get(0).getActivityName());
        System.out.println(className + activityRegister.getActivityRegister().get(1).getActivityName());
        System.out.println(className + activityRegister.getActivityRegister().get(2).getActivityName());
    }

    /**
     * Opens a stream and writes the user objects to the stream and then creates a file.
     *
     * @param filename the name of the created file.
     */
    public void writeContacts(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeInt(userRegister.getUserList().size());
            for (int i = 0; i < userRegister.getUserList().size(); i++) {
                oos.writeObject(userRegister.getUserList().get(i));
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a textfile from the files folder and adds them to the contacts array. Then sets the contacts array to oldContactList array.
     *
     * @param filename the read filename.
     */
    public void readContacts(String filename) {
        File newFile = new File(filename);
        if (newFile.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                int size = ois.readInt();
                for (int i = 0; i < size; i++) {
                    try {
                        userRegister.getUserList().add((User) ois.readObject());
                    } catch (ClassNotFoundException | IOException e) {
                        System.out.println(e);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks whether the User who recently logged in is a new user or not.
     *
     * @param user the logged in User.
     * @return an updated User.
     */
    public User checkLoginUser(User user) {
        for (int i = 0; i < userRegister.getUserList().size(); i++) {
            if (userRegister.getUserList().get(i).getUserName().equals(user.getUserName())) {
                user = userRegister.getUserList().get(i);
                user.setUserType(UserType.SENDUSER);
            }
        }
        if (user.getUserType() == UserType.LOGIN) {
            user.setUserType(UserType.SENDWELCOME);
            userRegister.getUserList().add(user);
            writeContacts("files/users.txt");
            for (int i = 0; i < userRegister.getUserList().size(); i++) {
                System.out.println(userRegister.getUserList().get(i).getUserName());
            }
        }
        System.out.println(className + " Antal users i UserRegister " + userRegister.getUserList().size());
        return user;
    }

    public void sendActivity(String userName) {
        int nbrOfActivities = activityRegister.getActivityRegister().size();
        Random rand = new Random();
        int activityNbr = rand.nextInt(nbrOfActivities);
        Activity activityToSend = new Activity();
        activityToSend.setActivityName(activityRegister.getActivityRegister().get(activityNbr).getActivityName());
        activityToSend.setActivityInstruction(activityRegister.getActivityRegister().get(activityNbr).getActivityInstruction());
        activityToSend.setActivityInfo(activityRegister.getActivityRegister().get(activityNbr).getActivityInfo());
        activityToSend.setFromUser(userName);
        sendNewActivityBuffer.put(activityToSend);
    }

    public void createUserTimer(User user) {
        UserTimer userTimer = new UserTimer(this, user);
        userTimer.startTimer();
        userTimerHashMap.put(user.getUserName(), userTimer);
    }

    public void setUserTimer(User user, String userName) {
        int timeInterval = user.getNotificationInterval();
        userTimerHashMap.get(userName).setTimeInterval(timeInterval);
    }

    /**
     * Receives a User object from the online buffer and checks it value.
     */
    public void run() {
        while (true) {
            try {
                User user = onLineBuffer.get();
                UserType userType = user.getUserType();
                switch (userType) {
                    case LOGIN:
                        createUserTimer(user);
                        User updatedUser = checkLoginUser(user);
                        sendUserBuffer.put(updatedUser);
                        sendActivity(updatedUser.getUserName());

                        break;
                    case LOGOUT:

                        break;
                    case COMPLETEDACTIVITY:

                        break;

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerController controller = new ServerController(4343);
        controller.start();
    }
}
