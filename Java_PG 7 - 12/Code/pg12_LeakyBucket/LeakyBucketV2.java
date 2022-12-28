package com.pg12_LeakyBucket;

import java.util.ArrayList;
import java.util.Scanner;

public class LeakyBucketV2 {
    //Assume the clock to be ticking 1 per unit time
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> packetQueue = new ArrayList<>();
        int bucketSize, remainingSize, takenClocks = 0;

        System.out.print("Enter Bucket Size: ");
        bucketSize = sc.nextInt();
        remainingSize = bucketSize;

        System.out.print("Enter number of packets: ");
        int n = sc.nextInt();


        System.out.println("Enter the packet sizes: ");
        for (int i = 0; i < n; i++) {
            packetQueue.add(sc.nextInt());
        }

        int j = packetQueue.size() - 1;
        while (j >= 0) {
            if (packetQueue.get(j) < remainingSize) {
                System.out.println("Clock: " + takenClocks);
                System.out.println("Packet of size: " + packetQueue.get(j) + " sent");
                remainingSize -= packetQueue.get(j);
                packetQueue.remove(j);

            } else {
                System.out.println("Packet with size: " + packetQueue.get(j) + " dropped.");
                packetQueue.remove(j);
                takenClocks++;
//                System.out.println("Packet of size: " + packetQueue.get(j) + " will be sent in next clock cycle.");
                remainingSize = bucketSize;
            }
            j--;
        }
        System.out.println("Total clock ticks: " + takenClocks);
    }
}
