package org.sangraama.coordination;

public class ServerLocation {
    String URL = "";
    int port;
    String dir = "";

    public ServerLocation(String url, int port, String dir) {
        this.URL = url;
        this.port = port;
        this.dir = dir;
    }
    
    public ServerLocation(String url, int port) {
        this.URL = url;
        this.port = port;
    }

    public String getServerURL() {
        return this.URL;
    }

    public int getServerPort() {
        return this.port;
    }

    public String getDirectory() {
        return this.dir;
    }
}
