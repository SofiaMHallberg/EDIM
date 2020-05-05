package server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UserRegister {
    private Map<String,User> userHashMap;



    private LinkedList<User> userLinkedList;

    public UserRegister() {
        userHashMap = new HashMap<>();
        userLinkedList = new LinkedList<>();
    }

    public Map<String, User> getUserHashMap() {
        return userHashMap;
    }

    public void setUserHashMap(HashMap userList) {
        this.userHashMap = userList;
    }

    public LinkedList<User> getUserLinkedList() {
        return userLinkedList;
    }

    public void setUserLinkedList(LinkedList<User> userLinkedList) {
        this.userLinkedList = userLinkedList;
    }

    public void updateUser(User updatedUser) {
        userHashMap.remove(updatedUser.getUserName());
        userHashMap.put(updatedUser.getUserName(), updatedUser);

        for (User user : userLinkedList) {
            if (user.getUserName().equals(updatedUser.getUserName())) {
                userLinkedList.remove(user);
            }
        }
        userLinkedList.add(updatedUser);
    }
}
