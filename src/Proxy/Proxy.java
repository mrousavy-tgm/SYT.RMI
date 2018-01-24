package Proxy;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Statics;
import Server.Processor;
import Modules.Task;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A Proxy class for round robin load balancing
 */
public class Proxy implements LoadBalancer {
    private static final long serialVersionUID = 46L;
    private static Logger<String> _logger = JLogger.Instance;
    private Registry _registry;
    private List<Processor> _processors;
    private Iterator<Processor> _iter;

    public Proxy(int port) throws RemoteException {
        _processors = new ArrayList<>();
        _iter = _processors.iterator();

        LoadBalancer stub =
                (LoadBalancer) UnicastRemoteObject.exportObject(this, port);
        _registry = OpenRegistry(port);
        _registry.rebind(Statics.LOAD_BALANCER, stub);
        _registry.rebind(Statics.COMPUTE, stub);
    }

    private static Registry OpenRegistry(int port) throws RemoteException {
        try {
            return LocateRegistry.getRegistry(port);
        } catch (RemoteException e) {
            _logger.Log(Logger.Severity.Info, "No Registry found, " +
                    "creating new one..");
            return LocateRegistry.createRegistry(port);
        }
    }

    @Override
    public void add(Processor processor) throws RemoteException {
        _processors.add(processor);
        _iter = _processors.iterator();
        _logger.Log(Logger.Severity.Info,
                "Registered new Server: \"" + processor + "\"");
    }

    @Override
    public boolean remove(Processor processor) throws RemoteException {
        boolean success = _processors.remove(processor);
        _iter = _processors.iterator();
        if(success)
            _logger.Log(Logger.Severity.Info,
                    "Removed Server: \"" + processor + "\"");
        return success;
    }

    @Override
    public void shutdown() throws RemoteException {
        try {
            _registry.unbind(Statics.LOAD_BALANCER);
            _registry.unbind(Statics.COMPUTE);
        } catch (NotBoundException e) {
            _logger.Log(e);
        }

        for (Processor processor : _processors) {
            // Shutdown every server ?
        }
    }

    @Override
    public <T> T run(Task<T> t) throws RemoteException {
        //List<Processor> available = _processors.stream()
        //      .filter(p -> !p.busy()).collect(Collectors.toList());

        do {
            if (_iter.hasNext())
                _iter = _processors.iterator();

            Processor p = _iter.next();
            if (!p.busy()) {
                return p.run(t);
            }
        } while(true);
    }
}
