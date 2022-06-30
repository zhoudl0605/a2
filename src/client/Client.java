package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import core.Helper;
import core.IRepository;

public class Client {
    private static final int DEFAULT_UDP_PORT = 6231;

    public static void main(String[] args) throws Exception {
        int[] ports = discoveryRepository();

        // check size of ports
        if (ports == null || ports.length == 0) {
            return;
        }

        IRepository[] repositories = new IRepository[ports.length];
        for (int i = 0; i < ports.length; i++) {
            java.rmi.registry.Registry localRegistry;
            localRegistry = LocateRegistry.getRegistry(ports[i]);
            String rmi = localRegistry.list()[0];
            repositories[i] = Helper.connect(ports[i], Integer.parseInt(rmi));
        }

        repositories[0].add("a", 1);
        String result = repositories[0].getValue("a");

        int a = 1;

    }

    private static int[] discoveryRepository() {
        int[] ports = null;
        try {
            InetAddress group = InetAddress.getByName("224.0.0.0");
            MulticastSocket multicastSocket = new MulticastSocket(DEFAULT_UDP_PORT);
            multicastSocket.joinGroup(group);

            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.setReuseAddress(true);

            byte[] sendData = "DISCOVERY".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, DEFAULT_UDP_PORT);
            socket.send(sendPacket);

            byte[] recvBuf = new byte[1024];
            MulticastSocket receiveSocket = new MulticastSocket(DEFAULT_UDP_PORT);
            receiveSocket.joinGroup(group);
            receiveSocket.setSoTimeout(4000);
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            receiveSocket.receive(receivePacket);

            // We have a response, (from any server)
            System.out.println("\n> Received packet from " + receivePacket.getAddress().getHostAddress() + " : "
                    + receivePacket.getPort());
            String msg = new String(receivePacket.getData()).trim();
            String[] tokens = msg.split(" ");
            // check size of tokens, if not empty then process the message

            if (tokens.length > 1) {
                System.out.println("\n> Received Port: ");
                ports = new int[tokens.length - 1];
                for (int i = 1; i < tokens.length; i++) {
                    System.out.println("> " + tokens[i]);
                    ports[i - 1] = Integer.parseInt(tokens[i]);
                }
            }

            socket.close();
            return ports;
        } catch (Exception e) {
            System.out.println("> No Service Found");
        }

        return null;
    }

    private static IRepository[] getRepositories(int[] ports) {
        IRepository[] repositories = new IRepository[ports.length];
        for (int i = 0; i < ports.length; i++) {
            try {
                java.rmi.registry.Registry localRegistry;
                localRegistry = LocateRegistry.getRegistry(ports[i]);

                String rmi = localRegistry.list()[0];
                repositories[i] = Helper.connect(ports[i], Integer.parseInt(rmi));
            } catch (RemoteException e) {
                System.out.println("No repository found");
            }
        }

        return repositories;
    }
}
