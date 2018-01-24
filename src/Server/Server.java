package Server;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Compute;
import Modules.Helper;
import Modules.Task;
import Proxy.LoadBalancer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class Server implements Processor {
    private static final long serialVersionUID = 42L;
    private boolean _busy = false;
    private Compute _stub;
    private Registry _registry;
    private LoadBalancer _balancer;
    private String _name;

    public Server(String name, int port)
            throws RemoteException {
        _name = name;

        _stub = (Compute) UnicastRemoteObject.exportObject(this, port); //8001
        _registry = Helper.OpenRegistry(port);
        _registry.rebind(name, _stub);
    }

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
                    _name + ": Executed Task \"" + task.toString() +
                            "\", at " + new Date().toString());
            return result;
        } finally {
            _busy = false;
        }
    }

    @Override
    public void shutdown() throws RemoteException {
        try {
            _registry.unbind(_name);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } finally {
            UnicastRemoteObject.unexportObject(this, true);
        }
    }
}
