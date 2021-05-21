package com.company;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class Test implements  Runnable{
    int count;
    public Test(int count ){
        this.count=count;
        for (int i = 0; i <count ; i++) {
            Thread t = new Thread(this,"mamad");
            t.start();

        }
    }
    @Override
    public void run() {
        System.out.println(2);
    }
}

class Mainn {
    static String endOfLine = "\r\n";
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("parsijoo.ir",80);
            OutputStream outputStream=socket.getOutputStream();
            DataInputStream clientInputStream = new DataInputStream(socket.getInputStream());

            if (socket.isConnected()){
                System.out.println("YESSSS");
            }
            String request= "GET / HTTP/1.1\n" +
                    "Host: parsijoo.ir\n" +
                    "Connection: keep-alive\n" +
                    "Cache-Control: max-age=0\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Accept-Language: en-US,en;q=0.9,fa;q=0.8\n" +
                    "Cookie: JSESSIONID=78F126235C45979F5DF31924C3D00BA8; pj-ac=rBQ8o2Cnit2QVl6uCJL9Ag; extension_notify=true";
            outputStream.write(request.getBytes());
            outputStream.write(endOfLine.getBytes());
            outputStream.write(endOfLine.getBytes());
            outputStream.flush();
//            socket.close();
            String line;
            StringBuilder ssss=new StringBuilder("");
            while ((line = clientInputStream.readLine()) != null) {
                ssss.append(line);
                System.out.println(line);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
