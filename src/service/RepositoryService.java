package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import distribution.Registry;
import repository_implementation.Repository;

public class RepositoryService extends UnicastRemoteObject {
    private static RepositoryService instance = null;
    private Registry registry;
    private Repository repository;

    public static RepositoryService getInstance() {
        if (instance == null) {
            try {
                instance = new RepositoryService(null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public RepositoryService(Registry registry) throws RemoteException {
        super();

        this.registry = registry;

        int id = this.registry.getNextID();

        this.registry.register(String.valueOf(id));

        RepositoryService.instance = this;
    }
}
