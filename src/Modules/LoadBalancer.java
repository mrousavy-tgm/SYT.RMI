package Modules;

import java.rmi.RemoteException;

/**
 * A Proxy for balancing CPU load on multiple Servers
 */
public interface LoadBalancer extends Compute {
    void add(Processor processor) throws RemoteException;
    void remove(Processor processor) throws RemoteException;
}
