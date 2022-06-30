package core;

import java.rmi.RemoteException;

public interface IRepository extends IAggregate {
    public IRepository find(String id) throws RemoteException;
}
