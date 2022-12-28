package com.pg9_TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        String currDir = System.getProperty("user.dir");
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
