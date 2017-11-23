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

    private enum ComputeType {
        Pi,
        Fibonacci
    }

    private static BigDecimal calcFibonacci(Compute compute, int count) throws RemoteException {
        Fibonacci task = new Fibonacci(count);
        return compute.executeTask(task);
    }
    private static BigDecimal calcPi(Compute compute, int digits) throws RemoteException {
        Pi task = new Pi(digits);
        return compute.executeTask(task);
    }

    public static void main(String[] args) {
        Logger<String> logger = new JLogger(System.out);

        if (args.length != 3) {
            logger.Log(Logger.Severity.Error, "Invalid Arguments specified! arg[0] = Name, arg[1] = Port, arg[2] = ComputeType");
        }

        String name = args[0];
        int port = Integer.parseInt(args[1]);
        ComputeType type = ComputeType.Fibonacci;

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            Compute comp = (Compute) registry.lookup(name);

            switch(type) {
                case Fibonacci:
                    int count = Integer.parseInt(args[1]);
                    BigDecimal fib = calcFibonacci(comp, count);
                    logger.Log(Logger.Severity.Info, "Fibonacci calculated!: " + fib);
                    logger.Log(Logger.Severity.Info, fib.toString());
                    break;
                case Pi:
                    int digits = Integer.parseInt(args[1]);
                    BigDecimal pi = calcPi(comp, digits);
                    logger.Log(Logger.Severity.Info, "Pi calculated!: " + pi);
                    logger.Log(Logger.Severity.Info, pi.toString());
                    break;
            }
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
