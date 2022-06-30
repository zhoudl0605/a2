package core;

import java.rmi.RemoteException;

import repository_implementation.Repository;

public interface IDistributedRepository extends IRepository {
    public IAggregate aggregate(String[] repids) throws RemoteException;
}