package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("mamad");

        try {
            ProxyServer proxyServer = new ProxyServer("127.0.0.1",5000);
            proxyServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
