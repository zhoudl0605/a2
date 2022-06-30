package distribution.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import distribution.Registry;

public class UDPServer implements Runnable {
    private static final int DEFAULT_UDP_PORT = 6231;
    private Thread udpThread;
    MulticastSocket receiveDSocket;
    private Registry registry;
    private InetAddress group;

    public UDPServer(Registry registry) {
        try {
            this.registry = registry;
            group = InetAddress.getByName("224.0.0.0");
            receiveDSocket = new MulticastSocket(DEFAULT_UDP_PORT);
            receiveDSocket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] receiveBuff = new byte[1024]; // receiving buffer

            System.out.println("\nStarted UDP server, listening on Broadcast IP, port " + DEFAULT_UDP_PORT + "\n");
            System.out.println("> Ready to receive b-cast packets...");

            while (true) {
                DatagramPacket dPacket = new DatagramPacket(receiveBuff, receiveBuff.length);
                receiveDSocket.receive(dPacket); // receiving data
                System.out.println("> Received packet from " + dPacket.getAddress().getHostAddress()
                        + ":" + dPacket.getPort());
                String msg = new String(dPacket.getData(), dPacket.getOffset(), dPacket.getLength()).trim();
                System.out.println("> Received message: " + msg);

                String[] tokens = msg.split(" ");
                // check size of tokens, if not empty then process the message
                if (tokens.length > 0) {
                    String command = tokens[0];
                    String[] args = new String[tokens.length - 1];
                    for (int i = 1; i < tokens.length; i++) {
                        args[i - 1] = tokens[i];
                    }

                    String reply = "";
                    switch (command) {
                        case "DISCOVERY":
                            System.out.println("received DISCOVERY from " + dPacket.getAddress().getHostAddress());
                            // send back the registry information
                            reply = "REGISTRY";
                            int[] potrs = this.registry.getPorts();
                            if (potrs == null) {
                                break;
                            }

                            for (int i = 0; i < potrs.length; i++) {
                                reply += " " + potrs[i];
                            }

                            sendMsg(reply);
                            break;

                        case "ADD_REGISTRY":
                            int port = Integer.parseInt(args[0]);
                            registry.addPort(port);

                            break;

                        default:
                            break;
                    }
                }

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        udpThread = new Thread(this);
        udpThread.start();
    }

    public void sendMsg(String msg) {
        try {
            InetAddress group = InetAddress.getByName("224.0.0.0");
            byte[] sendBuff = msg.getBytes();
            DatagramPacket dPacket = new DatagramPacket(sendBuff, sendBuff.length, group,
                    DEFAULT_UDP_PORT);
            receiveDSocket.send(dPacket); // Send a response
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        sendMsg("LEAVE " + registry.getRegistryURI());
        receiveDSocket.close();
    }
}
