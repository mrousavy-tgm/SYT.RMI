package Client;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Fibonacci;
import Modules.Task;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

enum TaskType {
    Pi,
    Fibonacci
}

/*
 * A Client object for running Tasks on the Server
 *
 * @author  Marc Rousavy
 * @version 1.0
 */
public class Client {
    private Registry _registry;

    /*
     * Create a new Client and initialize the Registry
     *
     * @param host the given host name to get the registry from
     * @author  Marc Rousavy
     * @version 1.0
     */
    public Client(String host) throws RemoteException {
        _registry = LocateRegistry.getRegistry(host);
    }

    /*
     * Run a specific Task (stub) on the server
     *
     * @param stubName The name of the stub in the registry
     * @author  Marc Rousavy
     * @version 1.0
     */
//    public <T> T runOnServer(String stubName) throws RemoteException, NotBoundException {
//        Task<T> task = (Task<T>)_registry.lookup(stubName);
//        return task.run();
//    }
    public BigDecimal runOnServer(String stubName) throws RemoteException, NotBoundException {
        Fibonacci fib = (Fibonacci) _registry.lookup(stubName);
        return fib.run();
    }

    public static void main(String[] args) {
        Logger<String> logger = new JLogger(System.out);

        String host = null;
        int port = 1099;
        TaskType type = TaskType.Fibonacci;
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            type = TaskType.valueOf(args[2]);
        } catch (Exception e) {
            logger.Log(Logger.Severity.Error, "Invalid program args format!");
            logger.Log(e);
            throw new IllegalArgumentException("Invalid program args!");
        }

        Client client;
        try {
            client = new Client(host);
            BigDecimal fib = client.runOnServer(type.toString());
            logger.Log(Logger.Severity.Info, "Calculated Fibonacci: " + fib);
        } catch (RemoteException e) {
            logger.Log(Logger.Severity.Error, "Could not get registry!");
            throw new IllegalArgumentException("Hostname (arg[0]) invalid!");
        } catch (NotBoundException e) {
            logger.Log(Logger.Severity.Error, "Stub is not bound to Registry!");
            throw new IllegalArgumentException("Stub (arg[2]) invalid!");
        }
    }
}
