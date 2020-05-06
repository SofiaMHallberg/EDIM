package server;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * This class handles the logic of the in and out coming object.
 *
 * @author Sofia Hallberg & Chanon Borgström.
 * @version 1.0
 */

public class ServerController extends Thread {
    private HashMap<String, SocketStreamObject> socketHashMap;
    private HashMap<String, UserTimer> userTimerHashMap;
    private ReceiverServer receiverServer;
    private SenderServer senderServer;
    private UserRegister userRegister;
    private ActivityRegister activityRegister;
    private Random rand;
    private String className = "Class: ServerController ";
    private Buffer<Object> receiveBuffer;
    private Buffer<Object> sendBuffer;
    private String userFilePath = "files/users.dat";

    /**
     * Constructs all the buffers and servers and HashMaps that is needed.
     *
     * @param port the received port number.
     */
    public ServerController(int port) {
        receiveBuffer = new Buffer<>();
        sendBuffer = new Buffer();
        socketHashMap = new HashMap();
        receiverServer = new ReceiverServer(port, socketHashMap, receiveBuffer);
        senderServer = new SenderServer(socketHashMap, sendBuffer);
        userRegister = new UserRegister();
        readUsers(userFilePath);
        activityRegister = new ActivityRegister("files/activities.txt");
        userTimerHashMap = new HashMap<>();
        rand = new Random();

    }

    /**
     * Opens a stream and writes the user objects to the stream and then creates a file.
     *
     * @param filename the name of the created file.
     */
    public void writeUsers(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeInt(userRegister.getUserLinkedList().size());
            for (User user : userRegister.getUserLinkedList()) {
                oos.writeObject(user);
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
    public void readUsers(String filename) {
        File newFile = new File(filename);
        System.out.println(className + " file length: " + newFile.length());
        if (newFile.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                int size = ois.readInt();
                System.out.println(className + " size från filen: " + size);
                for (int i = 0; i < size; i++) {
                    try {
                        User user = (User) ois.readObject();
                        System.out.println(className + " username i readUsers " + user.getUserName());
                        userRegister.getUserHashMap().put(user.getUserName(), user);
                        userRegister.getUserLinkedList().add(user);
                    } catch (ClassNotFoundException | IOException e) {
                        System.out.println(e);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(className + " storleken på userRegister när det har uppdaterats från fil " + userRegister.getUserHashMap().size());
    }

    /**
     * Checks whether the User who recently logged in is a new user or not.
     *
     * @param user the logged in User.
     * @return an updated User.
     */
    public User checkLoginUser(User user) {
        if (userRegister.getUserHashMap().size() != 0) {
            System.out.println(className + " första if-satsen i checkLoginUser " + userRegister.getUserHashMap().size());
            if (userRegister.getUserHashMap().containsKey(user.getUserName())) { //userRegister.getUserHashMap().get(user.getUserName()).getUserName().equals(user.getUserName())
                System.out.println(className + " checkLoginUser inne i if-satsen");
                user = userRegister.getUserHashMap().get(user.getUserName());
                user.setUserType(UserType.SENDUSER);

            } else {
                user.setUserType(UserType.SENDWELCOME);
                userRegister.getUserHashMap().put(user.getUserName(), user);
                userRegister.getUserLinkedList().add(user);
                System.out.println(className + " antal element i userRegister innan den skrivs till fil " + userRegister.getUserHashMap().size());
                writeUsers(userFilePath);
            }
        } else {
            user.setUserType(UserType.SENDWELCOME);
            userRegister.getUserHashMap().put(user.getUserName(), user);
            userRegister.getUserLinkedList().add(user);
            writeUsers(userFilePath);
        }

        return user;
    }

    public void sendActivity(String userName) {
        User user = userRegister.getUserHashMap().get(userName);
        if (user.getDelayedActivity() != null) {
            sendBuffer.put(user.getDelayedActivity());
            user.setDelayedActivity(null);
        } else {
            int nbrOfActivities = activityRegister.getActivityRegister().size();
            int activityNbr = rand.nextInt(nbrOfActivities);
            Activity activityToSend = new Activity();
            activityToSend.setActivityName(activityRegister.getActivityRegister().get(activityNbr).getActivityName());
            activityToSend.setActivityInstruction(activityRegister.getActivityRegister().get(activityNbr).getActivityInstruction());
            activityToSend.setActivityInfo(activityRegister.getActivityRegister().get(activityNbr).getActivityInfo());
            activityToSend.setActivityUser(userName);
            sendBuffer.put(activityToSend);
        }
    }

    public void createUserTimer(User user) {
        UserTimer userTimer = new UserTimer(this, user);
        userTimer.startTimer();
        userTimerHashMap.put(user.getUserName(), userTimer);
    }

    public void removeUserTimer(String userName) {
        UserTimer userTimer = userTimerHashMap.get(userName);
        userTimer.stopTimer();
        userTimerHashMap.remove(userName);
    }

    public void setTimeInterval(String userName, int timeInterval) {
        User user = userRegister.getUserHashMap().get(userName);
        user.setNotificationInterval(timeInterval);
        userTimerHashMap.get(userName).updateUser(user);
    }

    public void logOutUser(String userName) {
        try {
            System.out.println(className + "logOutUser: " + userName + " socketHashMap " + socketHashMap.get(userName));
            sleep(5000);
            socketHashMap.get(userName).getOos().close();
            socketHashMap.get(userName).getOis().close();
            socketHashMap.get(userName).getSocket().close();
            socketHashMap.remove(userName);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setDelayedActivity(Activity activity) {
        String userName = activity.getActivityUser();
        User user = userRegister.getUserHashMap().get(userName);
        user.setDelayedActivity(activity);

        UserTimer userTimer = userTimerHashMap.get(userName);
        userTimer.setActiveDelay(true);
        userTimer.setDelayTimer(5);
        userTimer.startTimer();

    }

    public void updateUserInterval(User user) {
        UserTimer userTimer = userTimerHashMap.get(user.getUserName());
        userTimer.updateUser(user);
        userRegister.updateUser(user);
        int currentTime = userTimer.getCurrentTime();
        userTimer.stopTimer();
        userTimer.setCurrentTime(currentTime);
        userTimer.startTimer();

    }

    /**
     * Receives a User object from the online buffer and checks it value.
     */
    public void run() {
        while (true) {
            try {
                Object object = receiveBuffer.get();
                if (object instanceof User) {
                    User user = (User) object;
                    String userName = user.getUserName();
                    UserType userType = user.getUserType();
                    switch (userType) {
                        case LOGIN:
                            createUserTimer(user);
                            User updatedUser = checkLoginUser(user);
                            sendBuffer.put(updatedUser);
                            break;
                        case LOGOUT:
                            sendBuffer.put(user);
                            removeUserTimer(userName);
                            logOutUser(userName);
                            writeUsers(userFilePath);
                            break;
                        case SENDINTERVAL:
                            updateUserInterval(user);
                            break;


                    }
                } else if (object instanceof Activity) {
                    Activity activity = (Activity) object;
                    String userName = activity.getActivityUser();

                    if (activity.isCompleted()) {
                        userTimerHashMap.get(userName).startTimer();
                        System.out.println(className + activity.getActivityName() + " is Completed");

                    } else {
                        setDelayedActivity(activity);
                        System.out.println(className + activity.getActivityName() + " is delayed");
                    }
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
