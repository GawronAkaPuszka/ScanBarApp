package com.example.test4.ui.notifications;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTCPThread extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String ip;
    private final int port;
    private Context context;

    public ClientTCPThread(String ip, int port, Context context) {
        this.ip = ip;
        this.port = port;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if(in == null || "STOP_SESSION_CLIENT".equals(inputLine)) {
                    stopConnection();
                    break;
                } else
                    /*
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Got message", Toast.LENGTH_SHORT).show();
                        }
                    });
                    */
                {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String sendMessage(String msg) {
        Thread th = new Thread(new Runnable() { public void run() {
                out.println(msg);
        }});

        th.setName("sendMessage");
        th.start();
        if (msg.equals("STOP_SESSION")){
            return "Connection down";
        }

        return "Message sent";
    }

    public void stopConnection() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, sendMessage("STOP_SESSION"), Toast.LENGTH_SHORT).show();
            }
        });
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


