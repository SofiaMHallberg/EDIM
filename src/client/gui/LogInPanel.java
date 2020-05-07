package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogInPanel extends JPanel { //TODO: Fixa så att man kan trycka på krysset
    private JLabel lblUserName, lblWelcome;
    private JTextField tfUserName;
    private JButton btnLogIn;
    private JButton btnExit;
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
        tfUserName = new JTextField();
        tfUserName.setPreferredSize(new Dimension(100, 30));
        btnLogIn = new JButton("Logga in");
        btnExit = new JButton("Avsluta");
        setLayout(new FlowLayout(4));
        add(lblUserName, FlowLayout.LEFT);
        add(tfUserName, FlowLayout.CENTER);
        add(btnLogIn);
        add(btnExit);

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logInFrame.dispose();
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);

            }
        });
        btnLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = tfUserName.getText();
                if (!userName.equals("") && !userName.contains(" ")) {
                    mainFrame.sendUser(userName);
                    mainFrame.createMainFrame();
                    logInFrame.closeWindow();
                } else if (userName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Du måste välja ett användarnamn");
                } else {
                    JOptionPane.showMessageDialog(null, "Ditt användarnamn får inte innehålla mellanslag");

                }
            }
        });
    }

}