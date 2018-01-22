package Client;

import JavaLogger.JLogger;
import JavaLogger.Logger;
import Modules.Statics;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        Logger<String> logger = JLogger.Instance;

        String host = null;
        int port = 1099;
        TaskType type = TaskType.Fibonacci;
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            type = TaskType.valueOf(args[2]);
        } catch (Exception e) {
            logger.Log(Logger.Severity.Error, "Invalid program args format!");
            logger.Log(e);
            throw new IllegalArgumentException("Invalid program args!");
        }

        Client client;
        try {
            client = new Client(host);
            BigDecimal fib = client.runOnServer(type.toString());
            logger.Log(Logger.Severity.Info, "Calculated Fibonacci: " + fib);
        } catch (RemoteException e) {
            logger.Log(Logger.Severity.Error, "Could not get registry!");
            throw new IllegalArgumentException("Hostname (arg[0]) invalid!");
        } catch (NotBoundException e) {
            logger.Log(Logger.Severity.Error, "Stub is not bound to Registry!");
            throw new IllegalArgumentException("Stub (arg[2]) invalid!");
        }
    }
}
