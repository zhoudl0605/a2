package service;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.IDistributedRepository;
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

        Repository repository = new Repository();
        IDistributedRepository stub = (IDistributedRepository) UnicastRemoteObject.exportObject(repository, 0);
        int id = this.registry.getNextID();

        java.rmi.registry.Registry localRegistry = LocateRegistry.getRegistry(this.registry.getPort());
        try {
            localRegistry.bind("" + id, stub);
            System.out.println("\nRepository id: " + id + " bound to registry at " + this.registry.getPort());
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        // int port = this.registry.getPort();
        // int id = this.registry.getNextID();

        // try {
        // String host = InetAddress.getLocalHost().getHostAddress();
        // URI uri = new URI("rmi://" + host + ":" + port + "/" + id);
        // this.registry.register(String.valueOf(id), uri);

        // } catch (URISyntaxException | UnknownHostException e) {
        // e.printStackTrace();
        // }

        RepositoryService.instance = this;
    }
}
