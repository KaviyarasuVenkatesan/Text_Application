package com.example.messengerclient;

import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    Client(Socket socket) {
        try {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error in Client Constructor");
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

    public void sendMessageToServer(String messageToServer) {
        try {
            dataOutputStream.writeUTF(messageToServer);
            dataOutputStream.flush();
        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error in the Sending message");
            closeEverything(socket,dataOutputStream,dataInputStream);
        }
    }

    public void receiveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!socket.isClosed() && socket.isConnected()) {
                    try {
                        String messageFromServer = dataInputStream.readUTF();
                        Controller.addLabel(messageFromServer, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error in Receiving message");
                        closeEverything(socket,dataOutputStream,dataInputStream);
                        break;
                    }
                }
            }
        }).start();
    }
}
