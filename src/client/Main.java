package client;

import compute.Compute;
import utils.JLogger;
import utils.Logger;

import java.math.BigDecimal;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        Logger<String> logger = new JLogger(System.out);

        String name = "Compute";
        int port = 1099;

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            Compute comp = (Compute) registry.lookup(name);
            Fibonacci task = new Fibonacci(Integer.parseInt(args[1]));
            BigDecimal fib = comp.executeTask(task);
            logger.Log(Logger.Severity.Info, "Fibonacci calculated!: " + fib);
            logger.Log(Logger.Severity.Info, fib.toString());
        } catch (AccessException e) {
            logger.Log(Logger.Severity.Error, "Invalid Access! " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            logger.Log(Logger.Severity.Error, "Failed to retrieve remote info! " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            logger.Log(Logger.Severity.Error, "Specified port is not bound! " + e.getMessage());
            e.printStackTrace();
        }
    }
}
