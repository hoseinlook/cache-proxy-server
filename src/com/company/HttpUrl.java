package com.company;

public  class HttpUrl {

    private final String rawRequest;
    private String host;
    private int port;
    private String method;


    public HttpUrl(String rawRequest) {
        this.rawRequest = rawRequest;
        this.initial();

    }

    private void initial() {
        this.method = this.rawRequest.split(" ")[0];
        if (this.method.equals("CONNECT")) {
            String url = rawRequest.split(" ")[1];
            this.host = url.split(":")[0];
            this.port = Integer.parseInt(url.split(":")[1]);
            return;
        }
        System.out.println("METHOD " + this.method);
        this.host = new MyRegex().find("http://([^: /]*)", this.rawRequest);
        String strPort = new MyRegex().find("http://[^: ]*:(\\d[^\\/]*)", this.rawRequest);
        this.port = 80;
        if (strPort != null) {
            this.port = Integer.parseInt(strPort);
        }
        if (this.host == null) {
            this.host = new MyRegex().find("host: ([^:]*)", this.rawRequest);
            strPort = new MyRegex().find("host: [^:]*:(\\d[^\\/]*)", this.rawRequest);
            this.port = 443;
            if (strPort != null) {
                this.port = Integer.parseInt(strPort.trim());
            }
        }
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getMethod() {
        return this.method;
    }
}
