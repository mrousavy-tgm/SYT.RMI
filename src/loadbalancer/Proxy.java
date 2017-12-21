package loadbalancer;

import compute.Compute;
import compute.Task;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The Compute Server Proxy for balancing the load by the round robin concept
 */
public interface Proxy {
    ArrayList<Compute> list = new ArrayList<>();
    void register(Compute stub) throws RemoteException;
    void unregister(Compute stub) throws RemoteException;
    <T> T executeTask(Task<T> t) throws RemoteException;
}
