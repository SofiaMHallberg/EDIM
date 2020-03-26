package server;

import java.util.HashMap;

public class ServerController extends Thread{
    private Buffer<User> onLineBuffer;
    private Buffer<User> sendUserBuffer;
    private Buffer<User> sendNewUserBuffer;
    private Buffer<Activity> sendNewActivityBuffer;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private ConnectionServer connectionServer;
    private CommunicationServer communicationServer;
    private UserRegister userRegister;

    public ServerController(int port) {
        onLineBuffer=new Buffer();
        socketHashMap=new HashMap();
        sendUserBuffer=new Buffer();
        sendNewUserBuffer=new Buffer();
        sendNewActivityBuffer=new Buffer();
        connectionServer=new ConnectionServer(this, port, socketHashMap, onLineBuffer);
        communicationServer=new CommunicationServer(port, socketHashMap, sendUserBuffer, sendNewUserBuffer, sendNewActivityBuffer);
        userRegister=new UserRegister();
    }

    public User checkOnlineUsers(User user) {
       // for(User u:userRegister.getUserList()) {
        for(int i = 0; i<userRegister.getUserList().size(); i++) {
            if(userRegister.getUserList().get(i).equals(user)) {
                user=userRegister.getUserList().get(i);
                user.setUserType(UserType.SENDUSER);
            }
            else {
                user.setUserType(UserType.SENDWELCOME);
                System.out.println("checkOnlineUsers - else-satsen");
            }
        }
        return user;
    }

    public void checkUpdatedUser(User updatedUser) {
        UserType userType=updatedUser.getUserType();
        System.out.println("UserType: " + userType);
        switch (userType) {
            case SENDUSER:
                sendUserBuffer.put(updatedUser);
                System.out.println("Storlek på buffern: (sendUserBuffer) " + sendUserBuffer.size());

                break;
            case SENDWELCOME:
                sendNewUserBuffer.put(updatedUser);
                System.out.println("Storlek på buffern: (sendNew...)" + sendNewUserBuffer.size());
                break;
        }
    }

    public void testActivity() {
        Activity testActivity = new Activity("Test-activity");
        sendNewActivityBuffer.put(testActivity);
    }

    public void run() {
        while(true) {
            try {
                User user = onLineBuffer.get();
                UserType userType = user.getUserType();
                System.out.println("ServerController: 1 " + user.getUserName());
                switch (userType) {
                    case LOGIN:
                        userRegister.getUserList().add(user);
                        User updatedUser=checkOnlineUsers(user);
                        checkUpdatedUser(updatedUser);
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
        ServerController controller=new ServerController(3343);
        controller.start();
    }

}
