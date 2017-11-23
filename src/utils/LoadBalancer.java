package utils;

import compute.Compute;
import compute.Task;

import java.rmi.RemoteException;


public class LoadBalancer implements Compute {

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        return null;
    }
}
