//package com.mycompany.project1;

import java.io.*;
import java.net.*;
import java.util.Date;

/**
 * 
 Problem:
 * how to handle closed and reopened connections. I need to keep track of their IDs!
 * Their IDs are indexes of their threads positions in an array.
 * How will they remember their index once they close the connection? -> database required!
 */
public class MultithreadedChatServer3 extends Thread {
    
    private static int PORT = 11111;
    private ChatServerThread3[] thread = new ChatServerThread3[50];
    private int ChatServerThread3Count = 0; 
    private Thread task;
    
    public void receiveMessage(byte[] message, int senderID) {
        // need to resend messages to other users
        
        //System.out.println("Incoming data in bytes:" + message);
        System.out.println(new String(message));
        for (int i = 0; i < ChatServerThread3Count; i ++) {
            if (senderID != i) {
                thread[i].sendMessage(message);
            }
        }
    }
    
    public void rejectConnection(Socket connection) {
        // send rejection message
        // close connection
    }
    
    public void handleClosedConnection(int myID) {
        
    }
    
    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(PORT)) {
	    System.out.println("Waiting for connections...");
            while (ChatServerThread3Count < 50) {
                try {
                    Socket connection = server.accept();
                    thread[ChatServerThread3Count] = new ChatServerThread3(this, connection, ChatServerThread3Count);
                    task = new Thread(thread[ChatServerThread3Count]);
                    task.start();
                    ChatServerThread3Count += 1;
                } catch (IOException ex) {}
            }
            Socket connection = server.accept();
            this.rejectConnection(connection);
        } catch (IOException ex) {
            System.err.println("Cannot start the server..");
        }
    }
    
    public static void main(String[] args) {
        Thread listen = new MultithreadedChatServer3();
        listen.start();
    }
}
