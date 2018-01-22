package Server;

import Modules.Processor;
import Modules.Task;
import java.rmi.RemoteException;

public class Server implements Processor {
    private static final long serialVersionUID = 42L;
    private boolean _busy = false;

    public Server() {}

    @Override
    public boolean busy() throws RemoteException {
        return _busy;
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        return null;
    }
}
