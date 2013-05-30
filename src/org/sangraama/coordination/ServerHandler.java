package org.sangraama.coordination;

import org.sangraama.coordination.staticPartition.StaticServer;

public enum ServerHandler {
    INSTANCE;
    private StaticServer staticServer = null;

    private ServerHandler() {
        this.staticServer = new StaticServer();
    }

    public ServerLocation getServerLocation(float x, float y) {
        return staticServer.getServerLocation(x, y);
    }
    
    public ServerLocation getThriftServerLocation(float x, float y) {
        return staticServer.getThriftServerLocation(x, y);
    }
}
