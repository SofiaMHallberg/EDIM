package gui;

import client.ClientController;

import javax.swing.*;

/**
 * This class starts GUI window and awaits the users input for a user name.
 * @autor Carolin Nordström & Oscar Kareld.
 * @version 1.0
 */

public class MainFrame extends JFrame {
    private ClientController clientController;
    private MainPanel mainPanel;
    private LogInPanel logInPanel;
    private JFrame frame;
    private String userName;

    public MainFrame() {
        setupFrame();
    }
    /**
     * Receives a clientController object and opens call for the method which opens a GUI window.
     * @param clientController The received ClientController object.
     */
    public MainFrame(ClientController clientController) {
        this.clientController = clientController;
        setupFrame();
    }


    public void setupFrame() {
        logInPanel = new LogInPanel(this);

        //frame = new JFrame();
        setBounds(0, 0, 819, 438);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("EDIM");
        setResizable(true);			// Prevent user from change size
        setLocationRelativeTo(null);	// Start middle screen

       // add(logInPanel);
        setContentPane(logInPanel);
        pack();
        setVisible(true);
    }
    public void setUserName() {
        userName = JOptionPane.showInputDialog("Välj användarnamn:");
    }

    public String getUserName() {
        return userName;
    }

    public void showAppPanel(String userName) {
        this.userName = userName;
        mainPanel = new MainPanel(this, userName);

        setContentPane(mainPanel);
    }
    public static void main(String[] args) {
        LogInFrame logInFrame = new LogInFrame();
        //TODO: Det sista vi gjorde var att skapa en LogInFrame, som kommer skapas först. Först när användaren tryckt på knappen ska MainFrame skapas.
    }
}
