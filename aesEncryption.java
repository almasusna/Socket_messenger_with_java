/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mycompany.project1;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author almas
 */
public class aesEncryption {
    
    
    public static byte[] createByteKeyFromString (String keyword) {
        MessageDigest sha = null;
        byte[] key = null;
        try {
            key = keyword.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            return key;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    public static String fromByteToString(byte[] message) {
        String output = null;
        byte[] encoded = Base64.getEncoder().encode(message);
        output = new String(encoded);
        return output;
    }
    
    public static byte[] fromStringToByte(String message) {
        return Base64.getDecoder().decode(message);
    }
    
    public byte[] encryptWithAes(byte[] message, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            key = Arrays.copyOf(key, 16);
            SecretKey secretKey = new SecretKeySpec(key, "AES"); 
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(message);
        } catch (Exception e) { 
            e.printStackTrace();
        }
        return null;
    }
    
    public byte[] decryptWithAes(byte[] encryptedMessage, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            key = Arrays.copyOf(key, 16);
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        String theOpenKey = "uchiha";
        aesEncryption aes = new aesEncryption();
        
        //System.out.println(aes.fromByteToString(aes.createByteKeyFromString("what>>>?")));
        
        String message = "hi there";
        byte[] key = aes.createByteKeyFromString(theOpenKey);
        byte[] encryptedMessageInBytes = aes.encryptWithAes(message.getBytes(), key);
        
        //byte[] decryptedMessageInBytes = aes.decryptWithAes(encryptedMessageInBytes, key);
        
        String whatGoesToPipe = aes.fromByteToString(encryptedMessageInBytes);
        byte[] whatComesFromPipe = aes.fromStringToByte(whatGoesToPipe);
        byte[] decryptedMessageInBytes = aes.decryptWithAes(whatComesFromPipe, key);
        
        System.out.println("Plain message: " + message);
        System.out.println("Plain key: " + theOpenKey);
        System.out.println("Key hashed and converted to Base64: " + aes.fromByteToString(key));
        System.out.println("Encrypted message: " + whatGoesToPipe);
        
        System.out.println("Decrypted message: " + new String(decryptedMessageInBytes));
        
        
        
    }
}
