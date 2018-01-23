package Server;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Task;
import java.rmi.RemoteException;
import java.util.Date;

public class Server implements Processor {
    private static final long serialVersionUID = 42L;
    private boolean _busy = false;

    public Server() {}

    @Override
    public boolean busy() {
        return _busy;
    }

    @Override
    public <T> T run(Task<T> task) throws RemoteException {
        try {
            _busy = true;
            T result = task.run();
            JLogger.Instance.Log(Logger.Severity.Info,
                    "Executed Task \"" + task.toString() + "\", at " +
                            new Date().toString());
            return result;
        } finally {
            _busy = false;
        }
    }
}
