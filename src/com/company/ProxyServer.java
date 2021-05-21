package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
//                Thread t = new Thread(new RequestHandler(socket));
//                t.start();

                new RequestHandler(socket).run();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

class RequestHandler implements Runnable {
    String endOfLine = "\r\n";
    Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            Socket socket = this.clientSocket;
            System.out.println("connected " + socket.getInetAddress() + ": " + socket.getPort() + "to server");

            DataInputStream clientInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            String firstLine = "";
//            String finalStr = this.readToEnd(socket);
//            byte [] listtt=socket.getInputStream().readAllBytes();
//            String finalStr = new String(listtt);

            byte[] mamad = new byte[clientSocket.getInputStream().available()];
            clientSocket.getInputStream().read(mamad);
            String finalStr = new String(mamad);
            System.out.println(finalStr);

            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println(finalStr);
            String out = this.getResponse(finalStr);
            clientOutputStream.write(out.getBytes());
            clientOutputStream.write(endOfLine.getBytes());
//            clientOutputStream.write(endOfLine.getBytes());
            clientOutputStream.flush();

            clientOutputStream.close();
            socket.close();

            System.out.println(finalStr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse(String request) {
        try {
            String endOfLine = "\r\n";
            Socket socket = new Socket("parsijoo.ir", 80);

            OutputStream outputStream = socket.getOutputStream();

//            String request = "GET / HTTP/1.1\n" +
//                    "Host: mihanblog.com\n" +
//                    "Connection: keep-alive\n" +
//                    "Cache-Control: max-age=0\n" +
//                    "Upgrade-Insecure-Requests: 1\n" +
//                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36\n" +
//                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
//                    "Accept-Language: en-US,en;q=0.9,fa;q=0.8";
            outputStream.write(request.getBytes());
            outputStream.write(endOfLine.getBytes());
            outputStream.write(endOfLine.getBytes());
            outputStream.flush();
//            socket.close();
            String line;
            TimeUnit.SECONDS.sleep(1);

            StringBuilder res = new StringBuilder("");
            while (socket.getInputStream().available() != 0) {
                byte[] mamad = new byte[socket.getInputStream().available()];
                socket.getInputStream().read(mamad);
                String kz=new String(mamad);
                System.out.println(kz);
                res.append(kz);
            }
            System.out.println(res);

            socket.close();
            return res.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}


