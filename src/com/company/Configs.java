package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class Configs {

    public static String BLOCK_RESPONSE="HTTP/1.1 400 Bad Request\n" +
            "Content-Type: text/html; charset=us-ascii\n" +
            "Server: Microsoft-HTTPAPI/2.0\n" +
            "Date: Fri, 21 May 2021 16:16:05 GMT\n" +
            "Connection: close\n" +
            "Content-Length: 334\n" +
            "\n" +
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"\"http://www.w3.org/TR/html4/strict.dtd\">\n" +
            "<HTML><HEAD><TITLE>Bad Request</TITLE>\n" +
            "<META HTTP-EQUIV=\"Content-Type\" Content=\"text/html; charset=us-ascii\"></HEAD>\n" +
            "<BODY><h2>Bad Request - Invalid Hostname</h2>\n" +
            "<hr><p>HTTP Error 400. The request hostname is invalid.</p>\n" +
            "</BODY></HTML>";

    public  static ArrayList<String> BLOCKED_URL=new ArrayList<>();

    public  static CacheDisk CACHE_DISK= new CacheDisk();

    public  static  String getUrlsAsStr(){
        StringBuilder finall= new StringBuilder();
        for (String item :BLOCKED_URL) {
            finall.append(item).append('\n');
        }
        return finall.toString();
    }
}
