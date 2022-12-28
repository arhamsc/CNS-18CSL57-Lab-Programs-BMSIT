# CNS Lab Question 10 - UDP Sockets

## Question

Write a program on data gram socket for client/server to display the messages on client side,
typed at the server side.

### Points to Note: -

* UDP stands for "User Datagram Protocol"
* It is a transport layer protocol in OSI Model
* It is unreliable and connection-less protocol
* The UDP helps to establish low-latency and loss-tolerating connections establish over the network.
* Used by many Application Layer protocols like DHCP, NTP, DNS etc.

### Notes on the Program: - ___Important___

* Source and destination ports are specified in both the server and client side of the programs.
* A background thread name `backgroundReceiver` is used to receive the incoming messages from the client or the server without breaking the code.
* When client sends `exit` then both the programs terminate.

## Program

### UDP Server

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
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
```

### UDP Client

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
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
```
