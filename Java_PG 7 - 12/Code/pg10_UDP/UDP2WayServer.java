package com.pg10_UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP2WayServer {
    public static DatagramSocket serverSocket; //UDP has datagrams
    public static DatagramPacket dp; //This is the packet that will be received
    public static BufferedReader br; //For reading Input from std in
    public static InetAddress ia; //IP address to bind the server to
    public static byte[] buf = new byte[1024]; //Socket buffer capacity
    public static int dPort = 3000, sPort = 8000; //Source port is of the server and dport is of the client

    public static void main(String[] args) throws IOException {
        serverSocket = new DatagramSocket(sPort);

        dp = new DatagramPacket(buf, buf.length);
        br = new BufferedReader(new InputStreamReader(System.in));
        ia = InetAddress.getLocalHost();

        Thread receiveThread = new Thread("backgroundReceiver") { //This is to run the receiving of messages in the background
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        serverSocket.receive(dp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String str4 = new String(dp.getData(), 0, dp.getLength());
                    System.out.println("Client said : " + str4);
                    if (str4.equals("exit")) {
                        System.out.println("Terminated...");
                        System.exit(0);
                        break;
                    }
                }
            }
        };

        System.out.println("Server is Running...");
        receiveThread.start();

        while (true) {
            System.out.println("Enter Message: ");
            String str3 = br.readLine();
            buf = str3.getBytes();
            serverSocket.send(new DatagramPacket(buf, str3.length(), ia, dPort));
        }
    }
}
