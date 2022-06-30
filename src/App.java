
import java.net.URI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import core.IAggregate;
import core.IDistributedRepository;
import repository_implementation.Repository;
import distribution.Registry;
import service.RepositoryService;

public class App {
    public static void main(String[] args) throws Exception {
        // create registry server
        Registry registry = new Registry();
        // RepositoryService repositoryService = new RepositoryService(registry);

        while (!registry.isInitialized()) {
        }
        RepositoryService repositoryService = new RepositoryService(registry);

        int a = 100;
        // Regi

        // Repository repository = Repository.getInstance();

        // get arvguments from command line

        // check if there is another rmi service in the network
        // RegistryServerInfo serverInfo = UDPClient.getRmiAddress();

        // // TODO: check if the serverInfo is null
        // if (serverInfo != null) {
        // // connect to remote registry
        // Registry remoteRegistry = LocateRegistry.getRegistry(serverInfo.getHost(),
        // serverInfo.getPort());
        // String[] list = remoteRegistry.list();

        // System.out.println("\nRemote registry contains the following services: ");

        // for (String s : list) {
        // System.out.println(s);
        // }

        // }

        // UDPServer udpServer = new UDPServer();
        // udpServer.start();
        // RegistryServer registryServer = new RegistryServer();
        // registryServer.start();

        // Repository repository = new Repository();

    }
}
