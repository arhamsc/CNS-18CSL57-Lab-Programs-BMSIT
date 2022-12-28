package com.pg9_TCP;

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
