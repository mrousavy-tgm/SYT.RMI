package Server;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Proxy.LoadBalancer;
import Modules.Statics;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

    public static void main(String args[]) throws RemoteException {
        int port = PORT;

        SecurityManagerCheck();
        Registry registry = OpenRegistry(port);

        try {
            String hostname = System.getProperties().getProperty("java.rmi.server.hostname");
            _logger.Log(Logger.Severity.Debug, "RMI running on hostname: " + hostname);
        } catch (AccessControlException ex) {
            _logger.Log(Logger.Severity.Error, "Access denied - Could not read java.rmi.server.hostname!");
        }

        try {
            _logger.Log(Logger.Severity.Debug, "Starting load balancer..");
            LoadBalancer balancer = (LoadBalancer) registry.lookup(Statics.LOAD_BALANCER);

            for (int i = 0; i < 4; i++) {
                _logger.Log(Logger.Severity.Debug, "Creating Processor #" + i);
                balancer.add(new Server());
            }

            _logger.Log(Logger.Severity.Debug, "LoadBalancer successfully initialized!");
        } catch (NotBoundException e) {
            _logger.Log(e);
        }
    }
}
