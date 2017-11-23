package server;

import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {
        ComputeEngine engine = new ComputeEngine();
        int port = 0;
        String name = "Compute";

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            engine.start(name, port);
            System.out.println("Compute Engine successfully bound to port " + port + ", with name \"" + name + "\"");
        } catch (SecurityManagerException ex) {
            System.out.println("Invalid Security Manager! " + ex.getMessage());
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal Argument specified! " + ex.getMessage());
            ex.printStackTrace();
        } catch (RemoteException ex) {
            System.out.println("Java RMI Remote Exception! " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
