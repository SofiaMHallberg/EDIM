package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class creates a thread pool and handles the communication to the Client.
 * @autor Sofia Hallberg & Chanon Borgström.
 * @version 1.0
 */

public class CommunicationServer {
    private int port;
    private ObjectOutputStream oos;
    private String className="Package: Server. Class: CommunicationServer ";
    private LinkedList<WorkerThread> threadPool;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private Buffer<User> sendUserBuffer;
    private Buffer<Activity> sendNewActivityBuffer;


    /**
     * Receives all necessary data and then generates and starts the thread pool.
     * @param port the received port.
     * @param socketHashMap the received socketHashMap.
     * @param sendUserBuffer the received sendUserBuffer.
     * @param sendNewActivityBuffer the received sendNewActivityBuffer.
     */
    public CommunicationServer(int port, HashMap<String, SocketStreamObject> socketHashMap, Buffer<User> sendUserBuffer, Buffer<Activity> sendNewActivityBuffer) {
        this.port=port;
        this.socketHashMap=socketHashMap;
        this.sendUserBuffer=sendUserBuffer;
        this.sendNewActivityBuffer=sendNewActivityBuffer;
        this.threadPool=new LinkedList<>();
        generateThreadPool(1);
        startThreadPool();
    }

    /**
     * Generates a thread pool with the received int.
     * @param nbrOfConnections received number of connections.
     */
    public void generateThreadPool(int nbrOfConnections) {
        for(int i=0; i< nbrOfConnections; i++) {
            threadPool.add(new WorkerThread());
        }

    }

    /**
     * Starts the thread pool.
     */
    public void startThreadPool() {
        for (WorkerThread thread:threadPool) {
            thread.start();
        }

    }

    // Inner thread class: handles outgoing communication.
    private class WorkerThread extends Thread {

        /**
         * receives an object from the buffer and sends it to the Client.
         */
        public void run() {
            while(true) {
                try {
                    /*
                    User sendNewUser=sendNewUserBuffer.get();
                    System.out.println(className + sendNewUser);
                    oos=socketHashMap.get(sendNewUser.getUserName()).getOos();
                    oos.writeUTF("Welcome");
                    System.out.println(className + oos);

                     */

                    User sendUser=sendUserBuffer.get();
                    oos=socketHashMap.get(sendUser.getUserName()).getOos();
                    oos.writeObject(sendUser);

                    Activity sendNewActivity=sendNewActivityBuffer.get();
                    for(String userName:socketHashMap.keySet()) {
                        oos=socketHashMap.get(userName).getOos();
                        oos.writeObject(sendNewActivity);
                    }//TODO:  Få metoden att skicka både ett User och ett Activity object.
                     // xTODO:    - Möjligtvis med olika try/catch-satser? alt if/else-satser?



                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }



}
