package com.pg12_LeakyBucket;

import java.util.Scanner;

public class LeakyBucket {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter Buffer Size and Output Rate: ");
        int buffer = in.nextInt();
        int out_rate = in.nextInt();
        int curr_storage = 0; // No. of bytes stored currently in the buffer.
        int overflow = 0;   // No. of bytes overflowing
        int req_storage = 0; // total storage required to store the current bytes in the buffer and the incoming bytes.
        int option = 1;

        while (option > 0) {
            System.out.println("Enter Input Rate: ");
            int inp_rate = in.nextInt();
            if (buffer < curr_storage + inp_rate) {
                // Condition for the scenario when the required storage exceeds the buffer size (Overflow condition)
                req_storage = curr_storage + inp_rate;
                overflow = req_storage - buffer;
                curr_storage = req_storage - overflow - out_rate;
            } else {
                // Condition for the scenario when the required storage does not exceed the buffer size (No Overflow condition)
                req_storage = curr_storage + inp_rate;
                overflow = 0;
                curr_storage += inp_rate - out_rate; /*->Changed*/
            }
            System.out.println("Input Rate: " + inp_rate + "\tTotal Required Storage: " +
                    req_storage + "\tOverflow: " + overflow + "\tCurrent Storage after transmission: " + curr_storage);
            System.out.println("Enter 0 to exit or 1 to continue: ");
            option = in.nextInt();
        }
    }
}
