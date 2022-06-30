package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAggregate extends Remote {
    public int sum(String key) throws RemoteException;
}
