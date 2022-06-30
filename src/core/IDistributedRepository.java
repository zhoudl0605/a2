package core;

import java.rmi.RemoteException;

public interface IDistributedRepository extends IRepository {
    public IAggregate aggregate(String[] repids) throws RemoteException;
}