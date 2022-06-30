package distribution;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import core.Helper;
import core.IRegistry;
import core.IRepository;
import distribution.UDP.UDPClient;
import distribution.UDP.UDPServer;
import repository_implementation.Repository;

public class Registry implements IRegistry {
    private java.rmi.registry.Registry localRegistry;
    private int rmiPort = 8000;
    private URI[] registryPeers = new URI[0];
    private Map<String, URI> repositoryServicesMap;
    private UDPServer udpServer;
    private int[] ports;
    private Boolean initialized = false;

    public Registry() {
        // start registry server
        localRegistry = createRegistry();
        repositoryServicesMap = new java.util.HashMap<>();
        UDPClient udpClient = new UDPClient(this);
        udpClient.discovery();

        // start UDP discovery server
        udpServer = new UDPServer(this);
        // addPort(getPort());
        udpServer.start();
        udpServer.sendMsg("ADD_REGISTRY " + getPort());
        initialized = true;
    }

    public Boolean isInitialized() {
        return initialized;
    }

    public void addPort(int port) {
        if (this.ports == null) {
            this.ports = new int[1];
            this.ports[0] = port;
        } else {
            int[] newPorts = new int[this.ports.length + 1];
            for (int i = 0; i < this.ports.length; i++) {
                if (this.ports[i] == port) {
                    return;
                }
                newPorts[i] = this.ports[i];
            }
            newPorts[this.ports.length] = port;
            this.ports = newPorts;

            Repository repository = Repository.getInstance();
            repository.setRepositoryMap(getRepositoryMap());
        }
    }

    public void close() {
        udpServer.close();
    }

    public void register(String id) throws RemoteException {
        IRepository repository = new Repository();

        IRepository stub = (IRepository) UnicastRemoteObject.exportObject(repository, 0);

        localRegistry.rebind(id, stub);
    }

    public void unregister(String id) throws RemoteException {
        try {
            localRegistry.unbind(id);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] list() {
        String[] list = new String[0];

        if (this.ports == null) {
            return list;
        }

        try {
            for (int port : this.ports) {
                java.rmi.registry.Registry registry = LocateRegistry.getRegistry(port);
                String[] registryList = registry.list();
                if (registryList == null) {
                    continue;
                } else {
                    String[] newList = new String[list.length + registryList.length];
                    for (int i = 0; i < list.length; i++) {
                        newList[i] = list[i];
                    }
                    for (int i = list.length; i < newList.length; i++) {
                        newList[i] = registryList[i - list.length];
                    }
                    list = newList;
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public IRepository find(String id) throws RemoteException {
        for (int port : this.ports) {
            try {
                java.rmi.registry.Registry registry = LocateRegistry.getRegistry(port);
                IRepository repository = (IRepository) registry.lookup(id);
                if (repository != null) {
                    return repository;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public java.rmi.registry.Registry createRegistry() {
        java.rmi.registry.Registry localRegistry;
        try {
            // check if current port is already used
            localRegistry = LocateRegistry.createRegistry(rmiPort);
        } catch (RemoteException e) {
            System.out
                    .println("\nRegistry port " + rmiPort + " is already used, trying to use " + (rmiPort + 1) + "...");
            rmiPort++;
            localRegistry = createRegistry();
        }

        return localRegistry;
    }

    public int getNextID() {
        String[] list = this.list();
        int id = -1;

        if (list != null) {
            for (String s : list) {
                if (Integer.parseInt(s) > id) {
                    id = Integer.parseInt(s);
                }
            }
        }
        return id + 1;

    }

    public int getPort() {
        return rmiPort;
    }

    public URI getRegistryURI() {
        String host;
        URI uri = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            uri = new URI("rmi://" + host + ":" + rmiPort);
        } catch (UnknownHostException | URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;
    }

    public void addRepositoryPeer(URI uri) {
        if (registryPeers == null) {
            registryPeers = new URI[1];
            registryPeers[0] = uri;
        } else {
            URI[] newregistryPeers = new URI[registryPeers.length + 1];
            for (int i = 0; i < registryPeers.length; i++) {
                // check if this registry is already in the list
                if (registryPeers[i].equals(uri)) {
                    return;
                }

                newregistryPeers[i] = registryPeers[i];
            }
            newregistryPeers[registryPeers.length] = uri;
            registryPeers = newregistryPeers;
        }
    }

    public void removeRepositoryPeer(URI uri) {
        if (registryPeers == null) {
            return;
        } else {
            URI[] newregistryPeers = new URI[registryPeers.length - 1];
            int j = 0;
            for (int i = 0; i < registryPeers.length; i++) {
                // check if this registry is already in the list
                if (registryPeers[i].equals(uri)) {
                    continue;
                }

                newregistryPeers[j] = registryPeers[i];
                j++;
            }
            registryPeers = newregistryPeers;
        }
    }

    public Boolean isRepositoryPeerExist(URI uri) {
        if (registryPeers == null) {
            return false;
        } else {
            for (int i = 0; i < registryPeers.length; i++) {
                if (registryPeers[i].equals(uri)) {
                    return true;
                }
            }
            return false;
        }
    }

    public URI[] getregistryPeers() {
        return registryPeers;
    }

    public void addRepositoryService(String id, URI uri) {
        repositoryServicesMap.put(id, uri);
    }

    public Map<String, URI> getRepositoryServicesMap() {
        return repositoryServicesMap;
    }

    public Boolean isRepositoryServiceExist(String id) {
        return repositoryServicesMap.containsKey(id);
    }

    public int[] getPorts() {
        return ports;
    }

    public Map<String, IRepository> getRepositoryMap() {
        Map<String, IRepository> repositoryMap = new HashMap<>();
        int[] ports = getPorts();
        try {
            if (ports == null || ports.length == 0) {
                return repositoryMap;
            }

            for (int i = 0; i < ports.length; i++) {
                java.rmi.registry.Registry localRegistry;
                localRegistry = LocateRegistry.getRegistry(ports[i]);
                String[] list = localRegistry.list();

                if (list == null || list.length == 0) {
                    continue;
                }
                String id = list[0];
                IRepository repository = Helper.connect(ports[i], Integer.parseInt(id));
                repositoryMap.put(String.valueOf(id), repository);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return repositoryMap;
    }
}
