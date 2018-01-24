package Proxy;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Statics;
import Server.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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

    private static Registry OpenRegistry(int port) throws RemoteException {
        try {
            return LocateRegistry.getRegistry(port);
        } catch (RemoteException e) {
            _logger.Log(Logger.Severity.Info, "No Registry found, " +
                    "creating new one..");
            return LocateRegistry.createRegistry(port);
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

    public static void main(String[] args) {
        SecurityManagerCheck();
        HostnameCheck();
        int port = PORT;

        try {
            LoadBalancer balancer = new Proxy();
            LoadBalancer stub =
                    (LoadBalancer) UnicastRemoteObject.exportObject(balancer, 0);
            Registry registry = OpenRegistry(port);
            _logger.Log(Logger.Severity.Info,
                    "Balancer exported to registry.");

            for (int i = 0; i < 4; i++) {
                _logger.Log(Logger.Severity.Debug, "Creating Processor #" + i);
                balancer.add(new Server());
            }

            registry.rebind(Statics.LOAD_BALANCER, stub);
            registry.rebind(Statics.COMPUTE, stub);
            _logger.Log(Logger.Severity.Info,
                    "Round robin load balancer successfully bound.");
        } catch (Exception e) {
            _logger.Log(e);
        }
    }
}
