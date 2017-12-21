package loadbalancer;

import server.ComputeServer;
import utils.JLogger;
import utils.Logger;

public class Main {
    public static void main(String[] args) {
        Logger<String> logger = new JLogger(System.out);
        Proxy balancer = new LoadBalancer();
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        for (int i = 0; i < 4; i++) {
            String name = "Compute" + i;
            int port = 1099 + i;
            ComputeServer server = new ComputeServer(name, port); // Create a new server
            logger.Log(Logger.Severity.Info, "Created Server in registry with name \"" + name + "\" at " + port);

            try {
                balancer.register(server); // Register it to the proxy
                server.start(); // Start the server
                logger.Log(Logger.Severity.Info, "\"" + name + "\" started and registered.");
            } catch(Exception ex) {
                logger.Log(Logger.Severity.Error, "Could not start or register \"" + name + "\"! " + ex.getMessage());
                try {
                    server.stop();
                } catch(Exception iex) {
                    // can't even stop wtf happened
                    logger.Log(Logger.Severity.Error, "Could not stop \"" + name + "\"! " + ex.getMessage());
                }
            }
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 4; i++) {
            ComputeServer server = (ComputeServer) balancer.list.get(i);
            try {
                server.stop();
            } catch(Exception ex) {
                logger.Log(Logger.Severity.Error, "Cannot stop server \"" + server.getName() + "\"! " + ex.getMessage());
            }
        }
    }
}
