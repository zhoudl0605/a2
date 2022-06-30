package core;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRepository extends IAggregate {
    public IRepository find(String id) throws RemoteException;

    public void add(String key, Integer val) throws RemoteException;

    public void delete(String key) throws RemoteException;

    public String list() throws RemoteException;

    public String getValue(String key) throws RemoteException;

    public void set(String key, ArrayList<Integer> val) throws RemoteException;

}
