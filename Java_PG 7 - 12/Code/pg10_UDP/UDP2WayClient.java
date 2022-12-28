package com.pg10_UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP2WayClient {
    public static DatagramSocket clientSocket;
    public static DatagramPacket dp;
    public static BufferedReader br;
    public static InetAddress ia;
    public static byte buf[] = new byte[1024];
    public static int sPort = 3000, dPort = 8000; //Source port is of the client and destination port is of the server

    public static void main(String[] args) throws IOException {
        clientSocket = new DatagramSocket(sPort);

        dp = new DatagramPacket(buf, buf.length);
        br = new BufferedReader(new InputStreamReader(System.in));
        ia = InetAddress.getLocalHost();

        Thread receiveThread = new Thread("backgroundReceiver") {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        clientSocket.receive(dp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String str4 = new String(dp.getData(), 0, dp.getLength());
                    System.out.println("Server said : " + str4);
                }
            }
        };

        receiveThread.start();

        System.out.println("Client is Running...");
        System.out.println("Type some text if you want, else 'exit' to quit: ");

        while (true) {
            System.out.println("Enter Message: ");
            String message = new String(br.readLine());
            buf = message.getBytes(); //Convert into byte stream
            if (message.equals("exit")) { //To terminate the session
                System.out.println("Terminated..");
                clientSocket.send(new DatagramPacket(buf, message.length(), ia, dPort));
                System.exit(0);
                break;
            }
            clientSocket.send(new DatagramPacket(buf, message.length(), ia, dPort));
        }
    }
}
