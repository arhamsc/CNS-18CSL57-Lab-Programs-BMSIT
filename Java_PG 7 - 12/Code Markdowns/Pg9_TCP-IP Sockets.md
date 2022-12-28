# CNS Lab Question 9 - TCP/IP Sockets

## Question

Using TCP/IP sockets, write a client – server program to make the client send the file name
and to make the server send back the contents of the requested file if present.

### Points to Note: -

* TCP stands for "Transmission Control Protocol"
* It uses the client-server architecture
* It is a transport layer protocol in OSI Model
* Three-way handshaking is involved to establish connection
* Has three phases - Establishment, Data Transmission & Teardown
* Used by many Application Layer protocols like HTTP, HTTPS, FTP etc.

### Notes on the Program: - ___Important___

* Two parts - Client and Server
* Before starting the program create a file called _"Sample.txt"_ in the root folder from where the files are being compiled with some text inside
* Run the TCP Server program first and then the TCP Client program
* When asked for `Enter Server Address:` in the client side of the file, Enter: `127.0.0.1`

> * `127.0.0.1` is the default IPV4 Address for `localhost`

* When prompted for `Client />` enter the file name: `Sample.txt`
* After successfully copying it will generate a `Client_Sample.txt` in the root directory

## Program

### TCP Server

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        String currDir = System.getProperty("user.dir"); //to get the current directory
        ServerSocket serverSocket = null;
        Socket clientSideSocket = null;
        InputStream inputStream = null;
        BufferedReader br = null, contentRead = null;
        OutputStream outputStream = null;
        PrintWriter pWrite = null;

        try {
            serverSocket = new ServerSocket(5119); //Connecting to passive Server Socket
            System.out.println("Server ready for connection....");

            clientSideSocket = serverSocket.accept();
            System.out.println("Connection Established: " + clientSideSocket.getInetAddress() + "\nWaiting for Client Request.");
        } catch (IOException e) {
            System.out.println("Initial");
            e.printStackTrace();
        }

        while (true) {

            try {
                inputStream = clientSideSocket.getInputStream();

                br = new BufferedReader(new InputStreamReader(inputStream));

                String fileName = br.readLine(); //We assume that the client is sending a file name

                if (fileName.equals("exit")) {
                    System.out.println("Terminating....");
                    System.exit(0);
                    break;
                }

                outputStream = clientSideSocket.getOutputStream();
                pWrite = new PrintWriter(outputStream, true); //Used to write to the output stream

                contentRead = new BufferedReader(new FileReader(currDir + "/" + fileName)); //Reading the contents

                String str; //Temp variable

                while ((str = contentRead.readLine()) != null) {
                    pWrite.println(str);
                }
                pWrite.println("\0");

                System.out.println("\nFile Contents sent successfully\n");

                contentRead.close();


            } catch (FileNotFoundException e) {
                System.out.println("No such file.");
                pWrite.println("No file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pWrite.close();
        br.close();
        clientSideSocket.close();
        serverSocket.close();
    }
}
```

### TCP Client

```java
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Socket sock = null;
        OutputStream outputStream = null;
        PrintWriter pWrite = null;
        InputStream inputStream = null;
        BufferedReader socketRead = null;
        FileWriter writer = null;

        System.out.println("Enter Server Address: ");
        String address = scanner.nextLine();

        try {
            sock = new Socket(address, 5119);
        } catch (IOException e) {
            System.out.println("Socket Error");
            e.printStackTrace();
        }

        while (true) {
            try {

                System.out.println("Enter file name to send. Enter 'exit' to exit");
                System.out.print("Client /> ");
                String serverFileName = scanner.nextLine();

                outputStream = sock.getOutputStream();
                pWrite = new PrintWriter(outputStream, true); //To send the filename to server
                pWrite.println(serverFileName);

                if (serverFileName.equals("exit")) {
                    System.out.println("Terminating....");
                    System.exit(0);
                    break;
                }

                inputStream = sock.getInputStream(); //We get the file contents after this
                socketRead = new BufferedReader(new InputStreamReader(inputStream));

                String fileContent = "";
                String temp;
                writer = new FileWriter("Client_" + serverFileName);  //Writing to file
                writer.write(""); //Replacing the file
                while ((temp = socketRead.readLine()) != null) {
                    if (temp.equalsIgnoreCase("no file")) {
                        break;
                    }
                    if (temp.equals("\0")) {
                        break;
                    }
                    writer.append(temp + "\n");
                }

                if (temp.equalsIgnoreCase("no file")) {
                    System.out.println("No Such File Found.");
                    new File("Client_" + serverFileName).delete(); //Delete the non-existent file
                    writer.close();
                    continue;
                }

                System.out.println("File : " + serverFileName + " Received.");

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pWrite.close();
            socketRead.close();
            //The above two are taking input and output streams as parameter so closing them means closing the socket
            inputStream.close();
            outputStream.close();
            sock.close();
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
