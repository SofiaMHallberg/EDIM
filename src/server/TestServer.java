package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String ip;
    private int port;

    public TestServer(String ip, int port) {
        this.ip = ip;

        new Connection(port).start();
    }

    private class Connection extends Thread {
        private int port;

        public Connection(int port) {
            this.port = port;
        }
        public void run() {
            Socket socket = null;
            System.out.println("AgeServerC startad");
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Lyssnar på port nr " + serverSocket.getLocalPort());
                while(true) {
                    try {
                        socket = serverSocket.accept();
                        new ClientHandler(socket);
                        System.out.println("ClientHandler skapad, rad 36");
                    } catch(Exception e) {
                        System.err.println(e);
                        if(socket!=null)
                            socket.close();
                    }
                }
            } catch(IOException e) {
                System.err.println(e);
            }
            System.out.println("AgeServerC nere");
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ClientHandler(Socket socket) throws IOException {

            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            start();
            System.out.println("ClientHandlers tråd skapad");
        }

        public void run() {
            try {
                while (true) {
                    User user = (User) ois.readObject();
                    System.out.println("ois läst, 68");
                    oos.writeObject(user);
                    System.out.println("User-objekt skickat till oos, rad 70");

                    sleep(20000);
                    //Activity act = new Activity();
                    //oos.writeObject(act);

                    sleep(20000);
                    String str = "HEEEEEEJ!";
                    oos.writeObject(str);
                }
            } catch (Exception e) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e2) {
                    }
                }
            }
            System.out.println("Klient nerkopplad");
        }
    }

    public static void main(String[] args) {
        TestServer ts = new TestServer("127.0.0.1", 3343);
    }
/*
    public void run() {
        try {
            serverSocket = new ServerSocket();
            socket = serverSocket.accept();
            ois = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

 */
}
