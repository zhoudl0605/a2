package distribution.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import distribution.Registry;

public class UDPClient {
    private static final int DEFAULT_UDP_PORT = 6231;
    MulticastSocket multicastSocket;
    private Registry registry;
    private InetAddress group;

    public UDPClient(Registry registry) {
        try {
            this.registry = registry;
            group = InetAddress.getByName("224.0.0.0");
            multicastSocket = new MulticastSocket(DEFAULT_UDP_PORT);
            multicastSocket.joinGroup(group);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void discovery() {
        try {
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
                for (int i = 1; i < tokens.length; i++) {
                    System.out.println("> " + tokens[i]);
                    this.registry.addPort(Integer.parseInt(tokens[i]));
                }
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("> No Service Found");
        }
    }
}
