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
    private Buffer<User> loginLogoutBuffer;
    private Buffer<User> sendUserBuffer;
    private Buffer<Activity> sendNewActivityBuffer;
    private Buffer sendBuffer;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private HashMap<String, UserTimer> userTimerHashMap;
    private ReceiverServer receiverServer;
    private SenderServer senderServer;
    private UserRegister userRegister;
    private ActivityRegister activityRegister;
    private Random rand;
    private String className = "Class: ServerController ";

    /**
     * Constructs all the buffers and servers and HashMaps that is needed.
     *
     * @param port the received port number.
     */
    public ServerController(int port) {
        loginLogoutBuffer = new Buffer();
        socketHashMap = new HashMap();
        sendUserBuffer = new Buffer();
        sendNewActivityBuffer = new Buffer();
        sendBuffer = new Buffer();
        receiverServer = new ReceiverServer(this, port, socketHashMap, loginLogoutBuffer, sendBuffer);
        senderServer = new SenderServer(socketHashMap, sendUserBuffer, sendNewActivityBuffer, sendBuffer);
        userRegister = new UserRegister();
        readContacts("files/users.txt");
        activityRegister = new ActivityRegister("files/activities.txt");
        userTimerHashMap = new HashMap<>();
        rand = new Random();
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
                        User user = (User) ois.readObject();
                        userRegister.getUserList().put(user.getUserName(), user);
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
        if (userRegister.getUserList().get(user.getUserName()).getUserName().equals(user.getUserName())) {
            user = userRegister.getUserList().get(user.getUserName());
            user.setUserType(UserType.SENDUSER);
        } else {
            user.setUserType(UserType.SENDWELCOME);
            userRegister.getUserList().put(user.getUserName(), user);
            writeContacts("files/users.txt");
        }
        return user;
    }

    public void sendActivity(String userName) {
        int nbrOfActivities = activityRegister.getActivityRegister().size();
        int activityNbr = rand.nextInt(nbrOfActivities);
        Activity activityToSend = new Activity();
        activityToSend.setActivityName(activityRegister.getActivityRegister().get(activityNbr).getActivityName());
        activityToSend.setActivityInstruction(activityRegister.getActivityRegister().get(activityNbr).getActivityInstruction());
        activityToSend.setActivityInfo(activityRegister.getActivityRegister().get(activityNbr).getActivityInfo());
        activityToSend.setActivityUser(userName);
        sendUserBuffer.put(userRegister.getUserList().get(userName)); //test
        sendNewActivityBuffer.put(activityToSend);
        System.out.println(className + activityToSend.getActivityName());
    }

    public void createUserTimer(User user) {
        UserTimer userTimer = new UserTimer(this, user);
        userTimer.startTimer();
        userTimerHashMap.put(user.getUserName(), userTimer);
    }

    public void setTimeInterval(String userName, int timeInterval) {
        User user = userRegister.getUserList().get(userName);
        user.setNotificationInterval(timeInterval);
        userTimerHashMap.get(userName).updateUser(user);
    }

    /**
     * Receives a User object from the online buffer and checks it value.
     */
    public void run() {
        while (true) {
            //TODO skapa en metod för att hämta userobjekt och gör den syncronized.
            try {
                User user = loginLogoutBuffer.get();
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
