package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class Cache {
    public  static HashMap<String,String> URL_STR_RESPONSE_CACHE=new HashMap<>();
    public static HashMap<String,String> URL_PATH_OF_CACHED_FILE = new HashMap<>();

}

class CacheDisk{
    private final String BASE_DIRECTORY="./cached_files/";
    private  HashMap<String ,String > REQ_RESP;
    public  CacheDisk(){
        File file = new File(this.BASE_DIRECTORY);
        boolean bool = file.mkdir();
        if (bool){
            System.out.println("cache directory created successfully");
        }
        else {
            System.out.println("cache directory exists");
        }
        try {
            this.REQ_RESP=this.readOldCachedFiles();
        } catch (IOException e) {
            e.printStackTrace();
            this.REQ_RESP=new HashMap<>();
        }
    }
    public  boolean containsKey(String rawRequest){
        return this.REQ_RESP.containsKey(rawRequest);
    }

    public  String get(String key){
        return this.REQ_RESP.get(key);
    }

    public void   saveRequest(String rawRequest,String rawResponse) throws IOException {
        String fileName = Base64.getEncoder().encodeToString(rawRequest.getBytes());
        File file = new File(this.BASE_DIRECTORY+fileName);
        FileWriter fileWriter =new FileWriter(file);
        fileWriter.write(rawResponse);
        fileWriter.flush();
        fileWriter.close();

        this.REQ_RESP.put(rawRequest,rawResponse);

    }

    private HashMap<String,String > readOldCachedFiles() throws IOException {
        File[] files = new File(this.BASE_DIRECTORY).listFiles();
        HashMap<String , String > results= new HashMap<>();

        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                String base64_name =file.getName();
                try {

                    String rawRequest = new String(Base64.getDecoder().decode(base64_name));
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];

                    int trash=fis.read(data);
                    fis.close();

                    String rawResponse = new String(data, StandardCharsets.UTF_8);
                    results.put(rawRequest,rawResponse);

                }catch (IllegalArgumentException ignored){

                }

            }
        }
        return results;
    }
}
