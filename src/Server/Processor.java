package Server;

import Modules.Compute;

import java.rmi.RemoteException;

public interface Processor extends Compute {
    boolean busy() throws RemoteException;
}
