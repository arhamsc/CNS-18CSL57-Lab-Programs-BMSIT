package com.pg11_RSA;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSAAlgoV2 {
    public static void main(String[] args) {
        BigInteger p, q, e, n, d, phi;
        int maxLength = 1024; //bitLength
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);
        String message = "";

        //Calculating all constants
        p = BigInteger.probablePrime(maxLength, rand); //returns prime number
        q = BigInteger.probablePrime(maxLength, rand);

        n = p.multiply(q);

        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(maxLength / 2, rand);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            //gcd(phi, e) == 1 (True) and e < phi
            e.add(BigInteger.ONE);
        }

        d = e.modInverse(phi);

        //Reading the message and ciphering and deciphering
        System.out.print("Enter the message to be encrypted: ");
        message = sc.nextLine();

        System.out.println("Message to encrypt: " + message);

        byte[] messageByteArray = message.getBytes();

        System.out.println("Ascii values of message: " + bytesToAscii(messageByteArray));

        byte[] cipherText = (new BigInteger(messageByteArray)).modPow(e, n).toByteArray();

        System.out.println("Cipher Text is: " + new String(cipherText));

        byte[] decipherText = (new BigInteger(cipherText)).modPow(d, n).toByteArray();

        System.out.println("Decrypted Text is: " + new String(decipherText));
    }
    private static String bytesToAscii(byte[] cipher) { //Optional
        StringBuilder temp = new StringBuilder();
        for (byte b : cipher) {
            temp.append((int) b);
        }
        return temp.toString();
    }
}
