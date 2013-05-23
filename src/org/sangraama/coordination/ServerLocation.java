package org.sangraama.coordination;

public class ServerLocation {
    String URL = "";
    int port = 0;

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
}
