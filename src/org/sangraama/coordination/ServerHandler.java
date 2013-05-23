package org.sangraama.coordination;

import org.sangraama.coordination.staticPartition.StaticServer;

public enum ServerHandler {
    INSTANCE;
    private StaticServer staticServer = null;
    
    private ServerHandler(){
	this.staticServer = new StaticServer();
    }
    
    public ServerLocation getLocation(float x, float y){
	return new ServerLocation();
    }
}
