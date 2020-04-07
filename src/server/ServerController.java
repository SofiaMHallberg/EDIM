package server;

import java.util.HashMap;

/**
 * This class handles the logic of the in and out coming object.
 *
 * @version 1.0
 * @autor Sofia Hallberg & Chanon Borgström.
 */

public class ServerController extends Thread {
    private Buffer<User> onLineBuffer;
    private Buffer<User> sendUserBuffer;
    private Buffer<Activity> sendNewActivityBuffer;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private ConnectionServer connectionServer;
    private CommunicationServer communicationServer;
    private UserRegister userRegister;
    private String classname = "Server.ServerController";

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
        connectionServer = new ConnectionServer(this, port, socketHashMap, onLineBuffer);
        communicationServer = new CommunicationServer(port, socketHashMap, sendUserBuffer, sendNewActivityBuffer);
        userRegister = new UserRegister();
    }

    /**
     * Checks whether the User who recently logged in is a new user or not.
     *
     * @param user the logged in User.
     * @return an updated User.
     */
    public User checkOnlineUsers(User user) {
        // for(User u:userRegister.getUserList()) {
        for (int i = 0; i < userRegister.getUserList().size(); i++) {
            if (userRegister.getUserList().get(i).equals(user)) {
                user = userRegister.getUserList().get(i);
                user.setUserType(UserType.SENDUSER);
            } else {
                user.setUserType(UserType.SENDWELCOME);
                userRegister.getUserList().add(user);
                System.out.println(classname + user.getUserType() +  "checkOnlineUsers - else-satsen");
            }
        }
        if(user.getUserType() == UserType.LOGIN) {
            user.setUserType(UserType.SENDWELCOME);
        }
        System.out.println(classname + "checkOnlineUsers: " + user.getUserType() );
        return user;

    }

    public void testActivity() {
        Activity testActivity = new Activity("Test-activity");
        sendNewActivityBuffer.put(testActivity);
    }

    /**
     * Receives a User object from the online buffer and checks it value.
     */
    public void run() {
        while (true) {
            try {
                User user = onLineBuffer.get();
                UserType userType = user.getUserType();
                System.out.println("ServerController: 1 " + user.getUserName());
                switch (userType) {
                    case LOGIN:
                        User updatedUser = checkOnlineUsers(user);
                        System.out.println(classname + updatedUser.getUserType());
                        sendUserBuffer.put(updatedUser);
                        testActivity();
                        System.out.println("ServerController: 2 " + user.getUserName());

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
        ServerController controller = new ServerController(3343);
        controller.start();
    }

}
