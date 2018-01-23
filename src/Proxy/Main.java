package Proxy;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Statics;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    private static final int PORT = 1099;
    private static Logger<String> _logger = JLogger.Instance;


    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        int port = PORT;

        try {
            LoadBalancer balancer = new Proxy();
            LoadBalancer stub =
                    (LoadBalancer) UnicastRemoteObject.exportObject(balancer, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            _logger.Log(Logger.Severity.Info,
                    "Balancer exported to registry.");

            registry.rebind(Statics.LOAD_BALANCER, stub);
            registry.rebind(Statics.COMPUTE, stub);
            _logger.Log(Logger.Severity.Info,
                    "Round robin load balancer bound.");
        } catch (Exception e) {
            _logger.Log(e);
        }
    }
}
