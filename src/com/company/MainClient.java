package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void connect() {
        try {
            System.out.println("try connect");
            clientSocket = new Socket("10.0.0.56", 8888);
            System.out.println("accepted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dataSend() {
        new Thread(new Runnable() {

            Scanner in = new Scanner(System.in);
            boolean isThread = true;
            @Override
            public void run() {
                while (isThread) {
                    try {
                        String sendData = in.nextLine();
                        if (sendData.equals("/quit")) isThread = false;
                        else dataOutputStream.writeUTF(sendData);

                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void dataRecv() {
        new Thread(new Runnable() {
            boolean isThread = true;

            @Override
            public void run() {
                while (isThread) {
                    try {
                        String recvData = dataInputStream.readUTF();
                        if (recvData.equals("/quit")) isThread = false;
                        else System.out.println("server : " + recvData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                closeAll();
                System.out.println("the system is closed");

            }
        }).start();
    }

    public void streamSetting() {
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() {
        try {
            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainClient() {
        connect();
        streamSetting();
        dataSend();
        dataRecv();
    }

    public static void main(String[] args) {
        new MainClient();
    }
}
