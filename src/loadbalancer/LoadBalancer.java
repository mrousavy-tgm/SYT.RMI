package loadbalancer;

import compute.Compute;
import compute.Task;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class LoadBalancer implements Proxy {
    private int _current;

    public LoadBalancer() {
        _current = 0;
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
