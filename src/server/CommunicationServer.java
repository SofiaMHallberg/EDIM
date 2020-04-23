package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class creates a thread pool and handles the communication to the Client.
 *
 * @version 1.0
 * @autor Sofia Hallberg & Chanon Borgström.
 */

public class CommunicationServer {
    private ObjectOutputStream oos;
    private String className = "Class: CommunicationServer ";
    private LinkedList<WorkerThread> threadPool;
    private HashMap<String, SocketStreamObject> socketHashMap;
    private Buffer<User> sendUserBuffer;
    private Buffer<Activity> sendNewActivityBuffer;
    private Buffer sendBuffer;


    /**
     * Receives all necessary data and then generates and starts the thread pool.
     *
     * @param socketHashMap         the received socketHashMap.
     * @param sendUserBuffer        the received sendUserBuffer.
     * @param sendNewActivityBuffer the received sendNewActivityBuffer.
     */
    public CommunicationServer(HashMap<String, SocketStreamObject> socketHashMap, Buffer<User> sendUserBuffer, Buffer<Activity> sendNewActivityBuffer, Buffer sendBuffer) {
        this.socketHashMap = socketHashMap;
        this.sendUserBuffer = sendUserBuffer;
        this.sendBuffer = sendBuffer;
        this.sendNewActivityBuffer = sendNewActivityBuffer;
        this.threadPool = new LinkedList<>();
        generateThreadPool(1);
        startThreadPool();
    }

    /**
     * Generates a thread pool with the received int.
     *
     * @param nbrOfConnections received number of connections.
     */
    public void generateThreadPool(int nbrOfConnections) {
        for (int i = 0; i < nbrOfConnections; i++) {
            threadPool.add(new WorkerThread());
        }
    }

    /**
     * Starts the thread pool.
     */
    public void startThreadPool() {
        for (WorkerThread thread : threadPool) {
            thread.start();
        }
    }

    // Inner thread class: handles outgoing communication.
    private class WorkerThread extends Thread {

        /**
         * receives an object from the buffer and sends it to the Client.
         */
        public void run() {
            while (true) {
                try {
                    System.out.println(className + "WorkerThread i början av try");
                    User sendUser = sendUserBuffer.get();
                    oos = socketHashMap.get(sendUser.getUserName()).getOos();
                    oos.writeObject(sendUser);
                    System.out.println(className + "WorkerThread i mitten av try");

                    Activity sendNewActivity = sendNewActivityBuffer.get();
                    oos = socketHashMap.get(sendNewActivity.getActivityUser()).getOos();
                    oos.writeObject(sendNewActivity);
                    //TODO: kolla upp till vem aktiviteten skickas till efter att timern klickas.
                    System.out.println(className + sendNewActivity.getActivityUser());

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
