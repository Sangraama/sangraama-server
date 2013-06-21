package org.sangraama.coordination;

import org.sangraama.asserts.SangraamaMap;
import org.sangraama.coordination.staticPartition.StaticServer;

public enum ServerHandler {
    INSTANCE;
    private StaticServer staticServer;

    private ServerHandler() {
        this.staticServer = new StaticServer();
    }

    public ServerLocation getServerLocation(float x, float y) {
        return staticServer.getServerLocation(x+SangraamaMap.INSTANCE.getOriginX(), y+SangraamaMap.INSTANCE.getOriginY());
    }
    
    public ServerLocation getThriftServerLocation(float x, float y) {
        return staticServer.getThriftServerLocation(x+SangraamaMap.INSTANCE.getOriginX(), y+SangraamaMap.INSTANCE.getOriginY());
    }
}
