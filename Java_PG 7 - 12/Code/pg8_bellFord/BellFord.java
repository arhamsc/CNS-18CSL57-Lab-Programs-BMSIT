package com.pg8_bellFord;

import java.util.Scanner;

public class BellFord {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[][] graph = new int[100][100];
        //Enter number of nodes
        System.out.println("Enter no of nodes: ");
        int n = in.nextInt();
        //Enter Adjacency Matrix of the Graph
        System.out.println("Enter Adjacency Matrix: ");
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                graph[j][k] = in.nextInt();
            }
        }
        //Enter Source Node
        System.out.println("Enter Source node: ");
        int src = in.nextInt();
        //Calculate Distance from src to all other nodes
        calc_distance(graph, n, src);
    }

    public static void calc_distance(int[][] g, int n, int src) {
        int i = 0, j = 0, k = 0;
        int[] d = new int[n];
        //Initialize Distance vector to all destination nodes 999
        for (i = 0; i < n; i++)
            d[i] = 999;
        d[src] = 0;
        //Bellman Ford Algorithm
        //Run outer loop n-1 times
        for (i = 0; i < n - 1; i++) {
            for (j = 0; j < n; j++) {
                for (k = 0; k < n; k++) {
                    //Bellman Ford Algorithm condition
                    if (d[k] > d[j] + g[j][k])
                        d[k] = d[j] + g[j][k];
                }
            }
        }
        //Checking for a negative cycle. If the above iteration runs for the nth time and the resulting
        //distance vector is different from the distance vector obtained in the (n-1)th iteration,
        //the graph is said to have a negative cycle. Bellman Ford Algorithm fails when the graph has a negative cycle.
        int[] c = new int[n];
        c = d;
        for (j = 0; j < n; j++) {
            for (k = 0; k < n; k++) {
                if (g[j][k] != 999 && c[k] > c[j] + g[j][k]) {
                    System.out.println("Graph contains a negative Cycle. Bellman Ford Algorithm Cannot be Applied");
                    return;
                }
            }
        }
        //Print the distance to each destination node from the source node.
        for (i = 0; i < n; i++) {
            if (i == src)
                continue;
            System.out.println(src + "->" + i + "=" + d[i]);
        }
    }
}
