package server;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

/**
 * This class handles the logic of the in and out coming objects from the clients.
 *
 * @author Carolin Nordstrom, Oscar Kareld, Chanon Borgstrom, Sofia Hallberg.
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
     * Reads a text file from the files folder and adds them to the contacts array. Then sets the contacts array to oldContactList array.
     *
     * @param filename the read filename.
     */
    public void readUsers(String filename) {
        File newFile = new File(filename);
        if (newFile.length() != 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                int size = ois.readInt();
                for (int i = 0; i < size; i++) {
                    try {
                        User user = (User) ois.readObject();
                        userRegister.getUserHashMap().put(user.getUsername(), user);
                        userRegister.getUserLinkedList().add(user);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
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
        if (userRegister.getUserHashMap().size() != 0) {

            if (userRegister.getUserHashMap().containsKey(user.getUsername())) { //userRegister.getUserHashMap().get(user.getUserName()).getUserName().equals(user.getUserName())
                user = userRegister.getUserHashMap().get(user.getUsername());
                user.setUserType(UserType.SENDUSER);
            } else {
                user.setUserType(UserType.SENDWELCOME);
                userRegister.getUserHashMap().put(user.getUsername(), user);
                userRegister.getUserLinkedList().add(user);
                writeUsers(userFilePath);
            }
        } else {
            user.setUserType(UserType.SENDWELCOME);
            userRegister.getUserHashMap().put(user.getUsername(), user);
            userRegister.getUserLinkedList().add(user);
            writeUsers(userFilePath);
        }
        return user;
    }

    /**
     * Receives a username and gets a User-object from the HashMap. If the user's activity has been delayed a delayed activity is sent to the sendBuffer,
     * else a new activity is sent to the client.
     *
     * @param username the received username.
     */
    public void sendActivity(String username) {
        User user = userRegister.getUserHashMap().get(username);
        if (user.getDelayedActivity() != null) {
            sendBuffer.put(user.getDelayedActivity());
            user.setDelayedActivity(null);
        } else {
            int nbrOfActivities = activityRegister.getActivityRegister().size();
            int activityNbr = rand.nextInt(nbrOfActivities);
            Activity activityToSend = new Activity();
            Activity getActivity = activityRegister.getActivityRegister().get(activityNbr);
            activityToSend.setActivityName(getActivity.getActivityName());
            activityToSend.setActivityInstruction(getActivity.getActivityInstruction());
            activityToSend.setActivityInfo(getActivity.getActivityInfo());
            activityToSend.setActivityUser(username);
            activityToSend.setActivityImage(getActivity.getActivityImage());
            sendBuffer.put(activityToSend);
            System.out.println("Sending activity: " + activityToSend.getActivityName());
        }
    }

    /**
     * Creates a UserTimer-object and starts the timer and puts the object in a UserTimerHashMap.
     *
     * @param user the received User which the UserTimer is connected to.
     */
    public void createUserTimer(User user) {
        UserTimer userTimer = new UserTimer(this, user);
        userTimer.startTimer();
        userTimerHashMap.put(user.getUsername(), userTimer);
    }

    /**
     * Retrieves a UserTimer object from the UserTimerHashMap and stops the received Timer.
     *
     * @param username the key that the UserTimerHashMap uses.
     */
    public void removeUserTimer(String username) {
        UserTimer userTimer = userTimerHashMap.get(username);
        userTimer.stopTimer();
        userTimerHashMap.remove(username);
    }

    /**
     * Receives the socket with the username and closes it and removes the socket from the HashMap.
     *
     * @param username
     */
    public void logOutUser(String username) {
        try {
            sleep(5000);
            socketHashMap.get(username).getOos().close();
            socketHashMap.get(username).getOis().close();
            socketHashMap.get(username).getSocket().close();
            socketHashMap.remove(username);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User logged out: " + username);
    }

    /**
     * Receives an activity object and gets a user with the help of the activity object and sets a user's delayed activity with the activity.
     * Then gets the connected userTimer and set the delay to 5min and set the activeDelay to true and then starts the Timer.
     *
     * @param activity the received activity.
     */
    public void setDelayedActivity(Activity activity) {
        String userName = activity.getActivityUser();
        User user = userRegister.getUserHashMap().get(userName);
        user.setDelayedActivity(activity);

        UserTimer userTimer = userTimerHashMap.get(userName);
        userTimer.setActiveDelay(true);
        userTimer.setDelayTimer(5);
        userTimer.startTimer();
    }

    /**
     * receives a UserTimer object and update the UserTimer's User object and stops the timer and
     * sets the currentTime with the recent currentTime and starts the timer.
     *
     * @param user the received User object.
     */
    public void updateUserInterval(User user) {
        UserTimer userTimer = userTimerHashMap.get(user.getUsername());
        userTimer.updateUser(user);
        userRegister.updateUser(user);
        int currentTime = userTimer.getCurrentTime();
        userTimer.stopTimer();
        userTimer.setCurrentTime(currentTime);
        userTimer.startTimer();
    }

    /**
     * Receives a User object from the receive-Buffer and checks if it's a User or a Activity.
     */
    public void run() {
        while (true) {
            try {
                Object object = receiveBuffer.get();

                if (object instanceof User) {
                    User user = (User) object;
                    String username = user.getUsername();
                    UserType userType = user.getUserType();

                    switch (userType) {
                        case LOGIN:
                            createUserTimer(user);
                            User updatedUser = checkLoginUser(user);
                            sendBuffer.put(updatedUser);
                            break;
                        case LOGOUT:
                            sendBuffer.put(user);
                            removeUserTimer(username);
                            logOutUser(username);
                            writeUsers(userFilePath);
                            break;
                        case SENDINTERVAL:
                            updateUserInterval(user);
                            break;
                    }
                } else if (object instanceof Activity) {
                    Activity activity = (Activity) object;
                    String username = activity.getActivityUser();

                    if (activity.isCompleted()) {
                        userTimerHashMap.get(username).startTimer();
                    } else {
                        setDelayedActivity(activity);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
