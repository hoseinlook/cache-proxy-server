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


            byte[] mamad = new byte[clientSocket.getInputStream().available()];
            clientSocket.getInputStream().read(mamad);
            String finalStr = new String(mamad);

            if (this.is_blocked(finalStr)) {
                clientOutputStream.write(Configs.BLOCK_RESPONSE.getBytes());
                clientOutputStream.write(endOfLine.getBytes());
                clientOutputStream.flush();

                clientOutputStream.close();
                socket.close();
                return;
            }

            String url = finalStr.split("\n")[0].trim();
            String out = null;
            if (Cache.URL_STR_RESPONSE_CACHE.containsKey(url)) {
                out=Cache.URL_STR_RESPONSE_CACHE.get(url);
                System.out.println("USING CACHE");
                clientOutputStream.write(out.getBytes());
                clientOutputStream.write(endOfLine.getBytes());
//            clientOutputStream.write(endOfLine.getBytes());
                clientOutputStream.flush();
                clientOutputStream.close();
                socket.close();
                return;

            }
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println(finalStr);
            String host = new MyRegex().find("http://([^: /]*)", finalStr);
            String port = new MyRegex().find("http://[^: ]*:(\\d[^\\/]*)", finalStr);
            int int_port = 80;
            if (port != null) {
                int_port = Integer.parseInt(port);
            }
            if (host == null) {
                host = new MyRegex().find("host: ([^:]*)", finalStr);
                port = new MyRegex().find("host: [^:]*:(\\d[^\\/]*)", finalStr);
                int_port = 443;
                if (port != null) {
                    int_port = Integer.parseInt(port.trim());
                }
            }
            out = this.handleHttp(finalStr.replaceAll("HTTP/1.1", "HTTP/1.0"), host, int_port);
            if (!Cache.URL_STR_RESPONSE_CACHE.containsKey(url) && out!=""){
                Cache.URL_STR_RESPONSE_CACHE.put(url,out);
            }
            clientOutputStream.write(out.getBytes());
            clientOutputStream.write(endOfLine.getBytes());
//            clientOutputStream.write(endOfLine.getBytes());
            clientOutputStream.flush();
            clientOutputStream.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String handleHttp(String request, String url, int port) {
        try {
            Socket socket;
            String endOfLine = "\r\n";
            socket = new Socket(url, port);
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(request.getBytes());
            outputStream.write(endOfLine.getBytes());
//            outputStream.write(endOfLine.getBytes());
            outputStream.flush();
//            socket.close();
            String line;
            TimeUnit.SECONDS.sleep(1);
            StringBuilder res = new StringBuilder("");

            while (socket.getInputStream().available() != 0) {
                byte[] mamad = new byte[socket.getInputStream().available()];
                socket.getInputStream().read(mamad);
                String kz = new String(mamad);
                System.out.println(kz);
                res.append(kz);
            }
            socket.close();
            return res.toString();


//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            int sizee = reader.read();
//            System.out.println(sizee);
//            while (true) {
//                String x = reader.readLine();
////                System.out.println(x);
//                if (x == null) {
//                    System.out.println(res);
//                    socket.close();
//                    return res.toString();
//                }
//                res.append(x).append("\r\n");
//            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean is_blocked(String body_str) {
        for (String item : Configs.BLOCKED_URL) {
            if (body_str.contains(item)) return true;
        }
        return false;
    }
}




class DataForwarder implements Runnable{
    private final InputStream input;
    private final OutputStream output;

    public DataForwarder(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[4096];
            int read = 1;
            while (read >= 0) {
                read = input.read(buffer);
                if (read > 0) {
                    output.write(buffer, 0, read);
                    if (input.available() < 1) {
                        output.flush();
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }
}

