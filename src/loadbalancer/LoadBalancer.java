package loadbalancer;

import compute.Compute;
import compute.Task;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoadBalancer implements Proxy {
    private int _current;
    private Registry _registry;

    public LoadBalancer() {
        _current = 0;
        try {
            _registry = LocateRegistry.createRegistry(2017);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(Compute stub) throws RemoteException {
        list.add(stub);
    }

    @Override
    public void unregister(Compute stub) throws RemoteException {
        list.remove(stub);
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        if (_current >= list.size()){
            _current %= list.size();
        }
        T result = list.get(_current).executeTask(t);
        _current++;
        _current %= list.size();
        return result;
    }
}
