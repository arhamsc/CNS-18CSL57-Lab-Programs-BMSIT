# CNS Lab Question 11 - RSA Encryption and Decryption

## Question

Write a program for simple RSA algorithm to encrypt and decrypt the data.

### Points to Note: -

* RSA Stands for Rivest Shamir Adleman Algorithm
* It is Asymmetric Encryption means that it uses two different keys for encryption
* It is used for Digital Signatures
* Two keys - Public Key and Private Key.

> * Public key is exposed to others whereas Private Key is maintained with the user.
> * No key exchanges are needed
> * _Public Key_ of the receiver is used to encrypt the message
> * Receiver can decrypt using his/her _Private Key_

* [RSA Algorithm GeeksForGeeks](https://www.geeksforgeeks.org/rsa-algorithm-cryptography/)
* Algorithm: - Two Stages -> Key Generation + Encryption/Decryption

> Key Generation
>>
>> Step 1: Choose two large prime numbers $p$ & $q$
>>
>> Step 2: Compute the following for $n$ and $\phi$ such that: $$ n = p*q $$ $$ \phi = (p-1)*(q-1) $$
>>
>> Step 3: Choose a number $e$ such that:
>> $$ 1<e<(p-1)*(q-1) $$
>>
>> Step 4: A number $d$ is selected such that: $$e*d \;\; \% \;\; \phi = 1 \;\; or \;\; d = e^{-1} \;\; \% \;\; (p-1)*(q-1)$$
>>
>> Step 5: Public key is function $P(n,e)$ and Private key is function $P(n,d)$
>>
> Encryption/Decryption
>> Encryption Function: - $$ cipher(c)=m^e \;\; \% \;\; n $$
>> Decryption Function: - $$ plainText(m)=c^d \;\; \% \;\; n$$

## Program

```java
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSAAlgo {
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
```
