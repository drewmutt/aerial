package aero.aerial.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by andrewsimmons on 6/21/15.
 */
public class UDPFindMeServer {
        public static void sendFindMe() {
            try {
                System.out.println("Sending find me..");
                String host = "239.0.0.0";
                int port = 2666;

                byte[] message = "127.0.0.1:8080".getBytes();

                // Get the internet address of the specified host
                InetAddress address = InetAddress.getByName(host);

                // Initialize a datagram packet with data and address
                DatagramPacket packet = new DatagramPacket(message, message.length,
                        address, port);

                // Create a datagram socket, send the packet through it, close it.
                DatagramSocket dsocket = new DatagramSocket();
                dsocket.send(packet);
                dsocket.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
}
