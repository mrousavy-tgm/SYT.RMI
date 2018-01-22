package Client;

import Modules.Task;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
    public <T> T runOnServer(String stubName) throws RemoteException, NotBoundException {
        Task<T> task = (Task<T>)_registry.lookup(stubName);
        return task.run();
    }
}
