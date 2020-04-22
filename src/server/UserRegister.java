package server;

import java.util.HashMap;

public class UserRegister {
    private HashMap<String,User> userList;

    public UserRegister() {
        userList = new HashMap<>();
    }
    public HashMap<String, User> getUserList() {
        return userList;
    }

    public void setUserList(HashMap userList) {
        this.userList = userList;
    }
}
