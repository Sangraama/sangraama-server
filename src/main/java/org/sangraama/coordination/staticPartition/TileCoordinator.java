package org.sangraama.coordination.staticPartition;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.jsonprotocols.send.SangraamaTile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class partition the game map of the server into sub tiles. Then it stores sub tile
 * origins with server URL in Hazelcast instance.
 * 
 * @author Dileepa Rajaguru
 * 
 */

public enum TileCoordinator {
    INSTANCE;
    private String TAG = "TileCoordinator: ";
    private Logger log = LoggerFactory.getLogger(TileCoordinator.class);

    private HazelcastInstance hazelcastInstance;

    Map<String, String> subtileMap;
    private float subTileHeight;
    private float subTileWidth;
    private SangraamaMap sangraamaMap;
    private String serverURL;
    private List<SangraamaTile> tileInfo;

    /**
     * Initialize a Hazelcast instance for the game server. Load the map details to partition the
     * game map.
     */
    public void init() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(new Config());
        this.subtileMap = hazelcastInstance.getMap("subtile");
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.subTileHeight = sangraamaMap.getSubTileWidth();
        this.subTileWidth = sangraamaMap.getSubTileHeight();
        this.serverURL = sangraamaMap.getHost();
    }

    /**
     * Generate sub tiles of the map partition allocated to the game server. Sub tile dimensions are
     * read from the game server configuration file.Generated sub tiles origins are stored with game
     * server URL in the hazelcast instance.
     */
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
        /*
         * Calculate and store coordination details of sub-tiles. Rationale : Changing/moving of
         * sub-tiles negligible with compared to game engine updating
         */
        this.tileInfo = this.calSubTilesCoordinations();
    }

    /**
     * Return the server URL where a specific point belongs to. The point can be any coordinate in
     * the whole game map.
     * 
     * @param x
     *            x coordinate of the point
     * @param y
     *            y coordinate of the point
     * @return URL of the host server
     */
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

        for (String key : keySet) {
            if (this.serverURL.equals(subtileMap.get(key))) {
                String[] s = key.split(":");
                tiles.add(new SangraamaTile(Float.parseFloat(s[0]), Float.parseFloat(s[1]),
                        this.subTileWidth, this.subTileHeight));
            }
        }
        log.info(TAG + "calculated size of tile (subtiles)");
        return tiles;
    }

    /**
     * Get details of sub-tiles coordinations belongs to the game server
     * 
     * @return ArrayList<SangraamaTile> about coordinations of sub-tiles
     */
    public List<SangraamaTile> getSubTilesCoordinations() {
        this.tileInfo = this.calSubTilesCoordinations();
        return this.tileInfo;
    }

    /**
     * 
     * @return Hazelcast instance used to store sub tiles
     */
    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

}
