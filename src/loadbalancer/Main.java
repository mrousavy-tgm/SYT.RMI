package loadbalancer;

import compute.Compute;
import server.ComputeServer;
import utils.JLogger;
import utils.Logger;

public class Main {

    public static void main(String[] args) {
        Logger<String> logger = new JLogger(System.out);
        Proxy balancer = new LoadBalancer();
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        for (int i = 0; i < 4; i++) {
            String name = "Compute" + i;
            int port = Compute.START_PORT + i;
            ComputeServer server = null;

            try {
                server = new ComputeServer(name, port); // Create a new server
                logger.Log(Logger.Severity.Info, "Created Server in registry with name \"" + name + "\" at " + port);
                server.start(); // Start the server
                logger.Log(Logger.Severity.Info, "\"" + name + "\" started and registered.");
            } catch(Exception ex) {
                logger.Log(Logger.Severity.Error, "Could not start or register \"" + name + "\"! " + ex.getMessage());
                try {
                    if (server != null)
                        server.stop();
                } catch(Exception iex) {
                    // can't even stop wtf happened
                    logger.Log(Logger.Severity.Error, "Could not stop \"" + name + "\"! " + ex.getMessage());
                }
            }
        }

        // Hook SIGTERM or any shutdown event so the sockets get closed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                ComputeServer server = (ComputeServer) balancer.list.get(i);
                try {
                    server.stop();  // Stop the servers again
                } catch(Exception ex) {
                    logger.Log(Logger.Severity.Error, "Cannot stop server \"" + server.getName() + "\"! " + ex.getMessage());
                }
            }
        }));
    }
}
