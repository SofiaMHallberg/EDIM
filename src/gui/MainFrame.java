package gui;

import client.ClientController;

import javax.swing.*;

/**
 * This class starts GUI window and awaits the users input for a user name.
 * @autor Carolin Nordström & Oscar Kareld.
 * @version 1.0
 */

public class MainFrame {
    private ClientController clientController;
    private String userName;

    public MainFrame() {
    }

    /**
     * Receives a clientController object and opens call for the method which opens a GUI window.
     * @param clientController The received ClientController object.
     */
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
