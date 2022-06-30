package core;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Helper {

    public static IRepository connect(int port, int id) {
        try {
            java.rmi.registry.Registry registry = LocateRegistry.getRegistry("localhost", port);

            return (IRepository) registry.lookup(String.valueOf(id));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
