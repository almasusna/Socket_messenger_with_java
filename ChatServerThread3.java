/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mycompany.project1;

import java.io.*;
import java.net.*;
import java.util.Date;

/**
 *
 * @author almas
 */
public class ChatServerThread3 implements Runnable {
    
    private Socket connection;
    private MultithreadedChatServer3 callback;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;
    private int myID;
    private int bytesToRead;
    private int bytesAvailable;
    
    public ChatServerThread3(MultithreadedChatServer3 callback, Socket connection, int myID) {
        this.callback = callback;
        this.connection = connection;
        this.myID = myID;
        try {
            streamIn = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
            streamOut = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
        } catch (IOException e) {}
        
    }
    
    public void sendMessage(byte[] message) {
        try {
            streamOut.write(message);
            streamOut.flush(); 
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    @Override
    public void run() {
        while (connection != null) {
            try {
                bytesAvailable = streamIn.available();
                if (bytesAvailable > 0) {
                    byte[] intArray = new byte[bytesAvailable];
                    streamIn.read(intArray, 0, bytesAvailable);
                    callback.receiveMessage(intArray, myID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            callback.handleClosedConnection(myID);
        
        }
    
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            System.err.println("Cannot close connection " + connection);
        }
    }
    

}
