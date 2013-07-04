package org.sangraama.coordination;

import java.util.Map;

import org.sangraama.asserts.SangraamaMap;

import com.hazelcast.core.Hazelcast;

public enum TileCoordinator {
    INSTANCE;
    Map<SangraamaMap, String> subtileMap;
    
    public void startTileCoordinator(){
        subtileMap = Hazelcast.getMap("subtile");
        subtileMap.put(SangraamaMap.INSTANCE,"8080");
    }
    
    public void printPort(){
        String s=subtileMap.get(SangraamaMap.INSTANCE);
    }
}
