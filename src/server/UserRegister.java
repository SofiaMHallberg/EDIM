package server;

import java.util.HashMap;
import java.util.Map;

public class UserRegister {
    private Map<String,User> userHashMap;

    public UserRegister() {
        userHashMap = new HashMap<>();
    }

    public Map<String, User> getUserHashMap() {
        return userHashMap;
    }

    public void setUserHashMap(HashMap userList) {
        this.userHashMap = userList;
    }

    public void updateUser(User updatedUser) {
        userHashMap.remove(updatedUser.getUserName());
        userHashMap.put(updatedUser.getUserName(), updatedUser);
    }
}
