package org.sangraama.coordination;

public enum ServerHandler {
    INSTANCE;
    private ServerHandler(){
	
    }
    
    public ServerLocation getLocation(){
	return new ServerLocation();
    }
    
    public class ServerLocation{
	String URL="";
	int port=0;
	
	
    }
}