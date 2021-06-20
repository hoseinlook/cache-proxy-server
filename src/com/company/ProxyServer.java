package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class ProxyServer {
    int port;
    String host;
    ServerSocket serverSocket;


    public ProxyServer(String host, int port) throws IOException {
        this.port = port;
        this.host = host;
        this.serverSocket = new ServerSocket(port);
    }


    public void start() {

        try {
            while (true) {
                System.out.println("server started at " + this.host + ": " + this.port);
                Socket socket = this.serverSocket.accept();
                Thread t = new Thread(new RequestHandler(socket));
                t.start();
//
//                new RequestHandler(socket).run();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}









