// import java.rmi.NotBoundException;
// import java.rmi.RemoteException;
// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;

// import distribution.UDP.UDPClient;
// import models.RegistryServerInfo;
// import repository.Repository;

// public class Client {
//     public static void main(String[] args) {
//         // find rmi service in the network
//         RegistryServerInfo serverInfo = UDPClient.();

//         if (serverInfo == null) {
//             System.out.println("No RMI service found in the network.");
//             return;
//         }

//         // connect to remote registry
//         try {
//             Registry remoteRegistry = LocateRegistry.getRegistry(serverInfo.getHost(), serverInfo.getPort());
//             Repository repository = (Repository) remoteRegistry.lookup("0");
//             System.out.println(repository.hello());
//         } catch (RemoteException | NotBoundException e) {
//             e.printStackTrace();
//         }
//     }
// }
