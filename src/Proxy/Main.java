package Proxy;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Server.Server;

import java.security.AccessControlException;

public class Main {
    private static final int PORT = 1099;
    private static Logger<String> _logger = JLogger.Instance;


    private static void SecurityManagerCheck() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            _logger.Log(Logger.Severity.Info, "No Security Manager available, " +
                    "created a new one.");
        }
    }

    private static void HostnameCheck() {
        try {
            String hostname = System.getProperties().getProperty("java.rmi.server.hostname");
            _logger.Log(Logger.Severity.Debug, "RMI running on hostname: " + hostname);
        } catch (AccessControlException ex) {
            _logger.Log(Logger.Severity.Error, "Access denied - Could not read java.rmi.server.hostname!");
        }
    }

    private static void ShutdownHook(LoadBalancer balancer) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    balancer.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                    _logger.Log(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        SecurityManagerCheck();
        HostnameCheck();
        int port = PORT;

        try {
            LoadBalancer balancer = new Proxy(port);
            _logger.Log(Logger.Severity.Info,
                    "Balancer exported to registry.");

            for (int i = 0; i < 4; i++) {
                _logger.Log(Logger.Severity.Debug, "Creating Processor #" + i);
                balancer.add(new Server());
            }

            ShutdownHook(balancer);
            _logger.Log(Logger.Severity.Info,
                    "Round robin load balancer successfully bound.");
        } catch (Exception e) {
            e.printStackTrace();
            _logger.Log(e);
        }
    }
}
