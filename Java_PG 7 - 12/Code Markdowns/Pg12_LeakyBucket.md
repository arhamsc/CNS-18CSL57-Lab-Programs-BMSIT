# CNS Lab Question 12 - Leaky Bucket

## Question

Write a program for congestion control using leaky bucket algorithm.

### Points to Note: -

* Leaky bucket is a Traffic Shaping Algorithm for congestion control
* It has Burst Inputs but constant Output Rate/Speed
* If there is overflow (inputRate > bucketBuffer) that packet is discarded

## Program

### __Implementation 1: -__

#### _Similar to given manual_

```java
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
```

### __Implementation 2: -__

#### _Implementation 1 is recommended for lab at BMSIT_

```java
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
```
