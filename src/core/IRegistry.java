package core;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;

import models.RegistryServerInfo;

public interface IRegistry extends IDirectory {
    public void register(String id, URI uri) throws RemoteException;

    public void unregister(String id) throws RemoteException;
}
