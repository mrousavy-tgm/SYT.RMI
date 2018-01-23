package Proxy;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Server.Processor;
import Modules.Task;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A Proxy class for round robin load balancing
 */
public class Proxy implements LoadBalancer {
    private static final long serialVersionUID = 46L;
    private static Logger<String> _logger = JLogger.Instance;
    private List<Processor> _processors;
    private Iterator<Processor> _iter;

    public Proxy() {
        _processors = new ArrayList<>();
        _iter = _processors.iterator();
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
