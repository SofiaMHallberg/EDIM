package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogInPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel lblUserName, lblWelcome;
    private JTextField tfUserName;
    private JButton btnLogIn;

    public LogInPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        createComponents();
        //setVisible(true);
    }

    public void createComponents() {
        setPreferredSize(new Dimension(400, 70));
        lblUserName = new JLabel("Username: ");
        lblWelcome = new JLabel("Welcome to EDIM, Every Day In Motion. Please enter your username below.");
        tfUserName = new JTextField();
        tfUserName.setPreferredSize(new Dimension(80, 20));
        btnLogIn = new JButton("Log In");
        setLayout(new FlowLayout(4));
        //add(lblWelcome, FlowLayout.LEFT);
        add(lblUserName, FlowLayout.LEFT);
        add(tfUserName, FlowLayout.CENTER);
        add(btnLogIn);
        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = tfUserName.getText();
                if (!userName.equals("") && !userName.contains(" ")) {
                    mainFrame.showAppPanel(userName);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Invalid username");
                }
            }
        });
    }
}
