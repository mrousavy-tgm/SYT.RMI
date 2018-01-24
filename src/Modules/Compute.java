package Modules;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {
    <T> T run(Task<T> t) throws RemoteException;
    void shutdown() throws RemoteException;
}
