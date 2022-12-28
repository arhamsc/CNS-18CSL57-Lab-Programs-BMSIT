package com.pg7_CRC;

import java.util.Scanner;

public class CRC {
    void div(int[] a, int k) //crc 16bit divison
    {
        int[] gp = {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1}; //intial expression
        int count = 0;
        for (int i = 0; i < k; i++) {
            if (a[i] == gp[0]) {
                for (int j = i; j < 17 + i; j++) {
                    a[j] = a[j] ^ gp[count++]; //long division
                }
                count = 0;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[100];
        int[] b = new int[100];
        int len, k;
        CRC ob = new CRC();
        System.out.println("Enter the length of Data Frame:");
        Scanner sc = new Scanner(System.in);
        len = sc.nextInt();
        int flag = 0;
        System.out.println("Enter the Message (enter 1 bit per line):");
        for (int i = 0; i < len; i++)
            a[i] = sc.nextInt();
        for (int i = 0; i < 16; i++)
            a[len++] = 0;
        k = len - 16;
        if (len >= 0) System.arraycopy(a, 0, b, 0, len);
        ob.div(a, k);
        for (int i = 0; i < len; i++)
            //user's message
            //division operation on client's side
            a[i] = a[i] ^ b[i];
        System.out.println("Data to be transmitted: ");
        for (int i = 0; i < len; i++)
            System.out.print(a[i] + " ");    //message sent from client's side
        System.out.println();
        System.out.println("Enter the Received Data: ");
        for (int i = 0; i < len; i++)
            a[i] = sc.nextInt();
        ob.div(a, k);
        for (int i = 0; i < len; i++) {
            if (a[i] != 0) {
                flag = 1;
                break;
            }
        }
        if (flag == 1)
            System.out.println("error in data");
        else
            System.out.println("no error");
        sc.close();
    }
}
