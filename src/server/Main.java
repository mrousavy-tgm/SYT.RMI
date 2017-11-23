package server;

import utils.JLogger;
import utils.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {
        Logger<String> logger = new JLogger(System.out);

        int port = 0;
        String name = "Compute";
        ComputeEngine engine = new ComputeEngine(name, port);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            engine.start();
            logger.Log(Logger.Severity.Info, "Compute Engine successfully bound to port " + port + ", with name \"" + name + "\"");
            engine.stop();
        } catch (SecurityManagerException ex) {
            logger.Log(Logger.Severity.Error, "Invalid Security Manager! " + ex.getMessage());
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            logger.Log(Logger.Severity.Error, "Illegal Argument specified! " + ex.getMessage());
            ex.printStackTrace();
        } catch (RemoteException ex) {
            logger.Log(Logger.Severity.Error, "Java RMI Remote Exception! " + ex.getMessage());
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            logger.Log(Logger.Severity.Error, "Engine was not bound, stopping failed! " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
