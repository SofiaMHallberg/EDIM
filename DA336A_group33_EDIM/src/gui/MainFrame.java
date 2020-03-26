package gui;

import client.ClientController;

import javax.swing.*;

public class MainFrame {
    private ClientController clientController;
    private String userName;

    public MainFrame() {
    }

    public MainFrame(ClientController clientController) {
        this.clientController = clientController;
        setUserName();
    }
    public void setUserName() {
        userName = JOptionPane.showInputDialog("Välj användarnamn:");
    }
    public String getUserName() {
        return userName;
    }
/*
    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
        mf.setUserName();
        System.out.println("Användaren heter: " + mf.getUserName());
    }

 */
}
