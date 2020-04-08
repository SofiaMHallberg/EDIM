package gui;

import javax.swing.*;

public class LogInFrame extends JFrame {

    public LogInFrame(MainFrame mainFrame) {
        setupFrame(mainFrame);
    }

    public void setupFrame(MainFrame mainFrame) {
        setBounds(0, 0, 200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("Login EDIM");
        setResizable(true);            // Prevent user from changing the size of the frame.
        setLocationRelativeTo(null);    // Start in the middle of the screen.
        LogInPanel logInPanel = new LogInPanel(this, mainFrame);
        setContentPane(logInPanel);
        pack();
        setVisible(true);
    }

    public void closeWindow(){
        dispose();
    }
}
