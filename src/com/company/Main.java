package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Configs.BLOCKED_URL.add("parsijoo.ir");
//        GUI gui = new GUI();

        try {
            ProxyServer proxyServer = new ProxyServer("127.0.0.1",5000);
            proxyServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
