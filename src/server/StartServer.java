package server;

/**
 * This class creates a server-object and starts the server.
 *
 * @author Carolin Nordström & Oscar Kareld & Chanon Borgström & Sofia Hallberg.
 * @version 1.0
 */

public class StartServer {
    public static void main(String[] args) {
        ServerController server = new ServerController(4343);
        server.start();
    }
}
