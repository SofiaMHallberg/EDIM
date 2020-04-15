package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogInPanel extends JPanel {
    private JLabel lblUserName, lblWelcome;
    private JTextField tfUserName;
    private JButton btnLogIn;

    private MainFrame mainFrame;
    private LogInFrame logInFrame;

    public LogInPanel(LogInFrame logInFrame, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.logInFrame = logInFrame;
        createComponents();
    }

    public void createComponents() {
        //setPreferredSize(new Dimension(200, 200));
        lblUserName = new JLabel("Användarnamn: ");
        tfUserName = new JTextField("Chanon");
        tfUserName.setPreferredSize(new Dimension(100, 20));
        btnLogIn = new JButton("Logga in");
        setLayout(new FlowLayout(4));
        add(lblUserName, FlowLayout.LEFT);
        add(tfUserName, FlowLayout.CENTER);
        add(btnLogIn);
        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = tfUserName.getText();
                if (!userName.equals("") && !userName.contains(" ")) {
                    mainFrame.showAppPanel(userName);
                    logInFrame.closeWindow();
                } else {
                    JOptionPane.showMessageDialog(null, "Inkorrekt användarnamn");
                }
            }
        });
    }
}
