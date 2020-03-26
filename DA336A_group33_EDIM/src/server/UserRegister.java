package server;

import java.util.LinkedList;

public class UserRegister {
    private LinkedList<User> userList;

    public UserRegister() {
        userList=new LinkedList<User>();
    }
    public LinkedList<User> getUserList() {
        return userList;
    }

    public void setUserList(LinkedList<User> userList) {
        this.userList = userList;
    }
}
