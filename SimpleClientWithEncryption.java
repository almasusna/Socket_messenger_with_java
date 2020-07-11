//package com.mycompany.project1;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author almas
 */
public class SimpleClientWithEncryption implements Runnable {
    
    private final static int PORT = 11111;
    private DataInputStream streamIn;
    private Socket connection;
    private DataOutputStream streamOut;
    private aesEncryption aes;
    private String keyword;
    private int bytesAvailable;
    
    
    public SimpleClientWithEncryption() {
        try {
            connection = new Socket("127.0.0.1", PORT); 
            System.out.println("Connected to server " + connection);
            try {
                streamIn = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
                streamOut = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            } catch (IOException e) {}
        } catch (IOException e) {
            System.out.println("Cannot connect to the server..");
        }
        /*
        finally {
            try {
                connection.close();
            } catch (IOException e) {
                System.err.println("Cannot close connection " + connection);
            }
        } */
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public void sendMessage(byte[] message) {
        try {
            streamOut.write(message);
            streamOut.flush(); 
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        byte[] key = aes.createByteKeyFromString(keyword);
        byte[] decryptedMessageInBytes = null;       
        String message;
        byte[] encryptedMessageInBytes = null;
        aes = new aesEncryption();
        byte[] decodedBytes = null;
        //aes.encryptWithAes(message.getBytes(), key);
        while (connection != null) {
            try {
                bytesAvailable = streamIn.available();
                if (bytesAvailable > 0) {
                    byte[] intArray = new byte[bytesAvailable];
                    streamIn.read(intArray, 0, bytesAvailable);
                    decryptedMessageInBytes = aes.decryptWithAes(intArray, key);
                    System.out.println(new String(decryptedMessageInBytes));
                }
                //decodedBytes = aes.fromStringToByte(message);
                
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("I am here");
    }
    
    
    public static void main(String[] args) {
        // Will listen for incoming data from Server
        //------------------------------------------
        SimpleClientWithEncryption client = new SimpleClientWithEncryption();
        aesEncryption aes = new aesEncryption();
        
        
        // Send messsages to the Server
        // INITIALIZATION ....
        Scanner myObj = new Scanner(System.in);
        System.out.print("Enter keyword: ");
        String keyword = myObj.nextLine();
        client.setKeyword(keyword);
        System.out.print("Enter username: ");
        String name = myObj.nextLine();
        System.out.println("You can start texting "+name +" ....");
        // -----------------------------------------------------------
        
        // Start of the listening thread
        
        Thread listen = new Thread(client);
        listen.start();
        // ------------------------------------------------------------------
        
        String message = null;
        String encryptedMessage = null;
        byte[] key = aes.createByteKeyFromString(keyword);
        byte[] encryptedMessageInBytes = null;
        //String encryptedMessageInString = null;
        
        
        
        while (true) {
            //System.out.print("$$:  ");
            message = myObj.nextLine();
            message = "*" + name + "* " + message;
            // now I need to encrypt the message
            encryptedMessageInBytes = aes.encryptWithAes(message.getBytes(), key);
            //encryptedMessageInString = aes.fromByteToString(encryptedMessageInBytes);
            client.sendMessage(encryptedMessageInBytes);
        }        
    }
}
