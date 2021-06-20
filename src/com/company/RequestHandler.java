package com.company;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public  class RequestHandler implements Runnable {
    String endOfLine = "\r\n";
    Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println("connected " + this.clientSocket.getInetAddress() + ": " + this.clientSocket.getPort() + "to server");
            DataOutputStream clientOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());

            String finalStr = getRawRequestFromSocket(this.clientSocket);

            if (this.is_blocked(finalStr)) {
                clientOutputStream.write(Configs.BLOCK_RESPONSE.getBytes());
                clientOutputStream.write(endOfLine.getBytes());
                clientOutputStream.flush();

                clientOutputStream.close();
                this.clientSocket.close();
                System.out.println("BLOCKED");
                return;
            }

            String url = finalStr.split("\n")[0].trim();
            String out;
            if (Cache.URL_STR_RESPONSE_CACHE.containsKey(url)) {
                out = Cache.URL_STR_RESPONSE_CACHE.get(url);
                System.out.println("USING CACHE");
                clientOutputStream.write(out.getBytes());
                clientOutputStream.write(endOfLine.getBytes());
                clientOutputStream.flush();
                clientOutputStream.close();
                this.clientSocket.close();
                return;

            }
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println(finalStr);
            HttpUrl httpUrl = new HttpUrl(finalStr);
            if (httpUrl.getMethod().equals("CONNECT")) {
                this.handleHttps( httpUrl.getHost(), httpUrl.getPort());
                System.out.println("https proxy forward");
                return;
            }
            out = this.handleHttp(finalStr.replaceAll("HTTP/1.1", "HTTP/1.0"), httpUrl.getHost(), httpUrl.getPort());
            if (!Cache.URL_STR_RESPONSE_CACHE.containsKey(url) && out != "") {
                Cache.URL_STR_RESPONSE_CACHE.put(url, out);
            }
            clientOutputStream.write(out.getBytes());
            clientOutputStream.write(endOfLine.getBytes());
//            clientOutputStream.write(endOfLine.getBytes());
            clientOutputStream.flush();
            clientOutputStream.close();
            this.clientSocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getRawRequestFromSocket(Socket clientSocket) throws IOException {
        byte[] mamad = new byte[clientSocket.getInputStream().available()];
        clientSocket.getInputStream().read(mamad);
        return new String(mamad);
    }

    public boolean is_blocked(String body_str) {
        for (String item : Configs.BLOCKED_URL) {
            if (body_str.contains(item)) return true;
        }
        return false;
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
                String kz = getRawRequestFromSocket(socket);
                System.out.println(kz);
                res.append(kz);
            }
            socket.close();
            return res.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleHttps( String host, int port) {

        Socket proxy = null;
        try {
            proxy = new Socket(host, port);
            String line = "HTTP/1.0 200 Connection established\r\nProxy-Agent: JavaProxy/1.0\r\n\r\n";
            String line2 = "HTTP/1.0 200 Connection established\r\n" +
                    "Proxy-Agent: ProxyServer/1.0\r\n" +
                    "\r\n";
            BufferedWriter proxyToClient = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
            proxyToClient.write(line);
            proxyToClient.flush();

            HttpsTransformer forwarder1 = new HttpsTransformer(this.clientSocket.getInputStream(), proxy.getOutputStream());
            Thread thread1 = new Thread(forwarder1);
            HttpsTransformer forwarder2 = new HttpsTransformer(proxy.getInputStream(), this.clientSocket.getOutputStream());

            thread1.start();
            forwarder2.run();
            this.clientSocket.close();
            proxy.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
