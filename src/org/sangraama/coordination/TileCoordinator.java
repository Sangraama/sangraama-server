package org.sangraama.coordination;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.sangraama.asserts.SangraamaMap;

import com.hazelcast.core.Hazelcast;

public enum TileCoordinator {
    INSTANCE;
    private String TAG = "TileCoordinator : ";
    Map<String, String> subtileMap;
    private float subTileHeight;
    private float subTileWidth;
    private SangraamaMap sangraamaMap;
    private String serverURL;

    TileCoordinator() {
        subtileMap = Hazelcast.getMap("subtile");
        sangraamaMap = SangraamaMap.INSTANCE;
        subTileHeight = sangraamaMap.getSubTileWidth();
        subTileWidth = sangraamaMap.getSubTileHeight();
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/sangraamaserver.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverURL = prop.getProperty("server");
    }

    public void generateSubtiles() {
        String subTileOrigins;
        float subTileOriginX, subTileOriginY;
        for (int i = 0; i < sangraamaMap.getMapWidth() / subTileWidth; i++) {
            for (int j = 0; j < sangraamaMap.getMapHeight() / subTileHeight; j++) {
                subTileOriginX = (i * subTileWidth) + sangraamaMap.getOriginX();
                subTileOriginY = (j * subTileHeight) + sangraamaMap.getOriginY();
                subTileOrigins = Float.toString(subTileOriginX) + ":"
                        + Float.toString(subTileOriginY);
                subtileMap.put(subTileOrigins, serverURL);
            }
        }
    }

    public String getSubTileHost(float x, float y) {
        String host = "";
        float currentSubTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        float currentSubTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        host = (String) Hazelcast.getMap("subtile")
                .get(Float.toString(currentSubTileOriginX) + ":"
                        + Float.toString(currentSubTileOriginY));
        return host;
    }

    public void printEntriesInSubtileMap() {
        Collection<String> values = subtileMap.values();
        for (String value : values) {
            System.out.println(TAG + value);
        }
        Set<String> keyset = subtileMap.keySet();
        for (String key : keyset) {
            System.out.println(TAG + key);
        }
    }
}
