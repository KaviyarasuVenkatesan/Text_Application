package com.example.messengerserver;

import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    Server(ServerSocket serverSocket){
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error in Server Constructor");
            closeEverything(socket,dataOutputStream,dataInputStream);
        }
    }

    public static void closeEverything(Socket socket , DataOutputStream dataOutputStream , DataInputStream dataInputStream){
        try
        {
            if(dataInputStream != null)
                dataInputStream.close();

            if (dataOutputStream != null)
                dataOutputStream.close();

            if (socket != null)
                socket.close();

        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error in the CloseEverything");
        }
    }

    public void sendMessageToClient(String messageToClient) {
        try {
            dataOutputStream.writeUTF(messageToClient);
            dataOutputStream.flush();
        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error in the Sending message");
            closeEverything(socket,dataOutputStream,dataInputStream);
        }
    }

    public void receiveMessageFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!socket.isClosed() && socket.isConnected()) {
                    try {
                        String messageFromClient = dataInputStream.readUTF();
                        Controller.addLabel(messageFromClient, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error in Receiving message");
                        closeEverything(socket,dataOutputStream,dataInputStream);
                    }
                }
            }
        }).start();
    }

}
