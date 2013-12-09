package org.sangraama.coordination.dynamic;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.sangraama.coordination.MapCoordinator;
import org.sangraama.coordination.SangraamaMap;
import org.sangraama.jsonprotocols.send.SangraamaTile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum SubTileCoordinator implements MapCoordinator {
    INSTANCE;
    private String TAG = "SubTileCoordinator: ";
    private Logger log = LoggerFactory.getLogger(SubTileCoordinator.class);

    private HazelcastInstance hazelcastInstance;

    Map<String, String> subtileMap;
    private float subTileHeight;
    private float subTileWidth;
    private SangraamaMap sangraamaMap;
    private String serverURL;
    private List<SangraamaTile> tileInfo;

    SubTileCoordinator() {
    }

    public void init() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(new Config());
        this.subtileMap = hazelcastInstance.getMap("subtile");
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.subTileHeight = sangraamaMap.getSubTileWidth();
        this.subTileWidth = sangraamaMap.getSubTileHeight();
        this.serverURL = sangraamaMap.getHost();
    }

    public void generateSubTiles() {
        String subTileOrigins;
        float subTileOriginX, subTileOriginY;
        for (int i = 0; i < sangraamaMap.getMapWidth() / subTileWidth; i++) {
            for (int j = 0; j < sangraamaMap.getMapHeight() / subTileHeight; j++) {
                subTileOriginX = (i * subTileWidth) + sangraamaMap.getOriginX();
                subTileOriginY = (j * subTileHeight) + sangraamaMap.getOriginY();
                subTileOrigins = Float.toString(subTileOriginX) + ":"
                        + Float.toString(subTileOriginY);
                subtileMap.put(subTileOrigins, serverURL);
                /*
                 * log.info(TAG + "host-" + serverURL + ", origin_x-" +
                 * subTileOriginX + ", origin_y-" + subTileOriginY);
                 */
                System.out.println(TAG + "host-" + serverURL + ", origin_x-" + subTileOriginX + ", origin_y-" + subTileOriginY);
            }
        }
        /*
         * Calculate and store coordination details of sub-tiles. Rationale : Changing/moving of
         * sub-tiles negligible with compared to game engine updating
         */
        this.tileInfo = this.calSubTilesCoordinations();
    }

    public String getSubTileHost(float x, float y) {
        String host = "";
        float subTileOriginX = x - (x % sangraamaMap.getSubTileWidth());
        float subTileOriginY = y - (y % sangraamaMap.getSubTileHeight());
        host = (String) subtileMap.get(Float.toString(subTileOriginX) + ":"
                + Float.toString(subTileOriginY));
        return host;
    }

    /**
     * Calculate (for storing purpose) coordination details of sub-tiles. Rationale :
     * Changing/moving of sub-tiles negligible with compared to game engine updating
     *
     * @return ArrayList<SangraamaTile> about coordinations of sub-tiles
     */
    private ArrayList<SangraamaTile> calSubTilesCoordinations() {
        ArrayList<SangraamaTile> tiles = new ArrayList<SangraamaTile>();
        Set<String> keySet = subtileMap.keySet();

        // Iterate though all keys
        for (String key : keySet) {
            // If sub-tile is inside current server, add to list
            if (this.serverURL.equals(subtileMap.get(key))) {
                String[] s = key.split(":");
                tiles.add(new SangraamaTile(Float.parseFloat(s[0]), Float.parseFloat(s[1]),
                        this.subTileWidth, this.subTileHeight));
            }
        }
        // log.info("calculated size of tile (subtiles)");
        return tiles;
    }

    /**
     * Get details of sub-tiles coordinations
     *
     * @return ArrayList<SangraamaTile> about coordinations of sub-tiles
     */
    public List<SangraamaTile> getSubTilesCoordinations() {
        this.tileInfo = this.calSubTilesCoordinations();
        return this.tileInfo;
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

}