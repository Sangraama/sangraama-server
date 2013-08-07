package org.sangraama.coordination.staticPartition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.coyote.http11.Http11Protocol;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.controller.clientprotocol.SangraamaTile;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public enum TileCoordinator {
    INSTANCE;
    private String TAG = "TileCoordinator : ";
    private boolean D = true;

    Map<String, String> subtileMap;
    private float subTileHeight;
    private float subTileWidth;
    private SangraamaMap sangraamaMap;
    private String serverURL;
    private ArrayList<SangraamaTile> tileInfo;

    TileCoordinator() {
        subtileMap = Hazelcast.getMap("subtile");
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.subTileHeight = sangraamaMap.getSubTileWidth();
        this.subTileWidth = sangraamaMap.getSubTileHeight();

        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
            this.serverURL = prop.getProperty("host") + ":" + prop.getProperty("port") + "/"
                    + prop.getProperty("dir") + "/sangraama/player";
            System.out.println(TAG + serverURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getHostPort();
    }

    /**
     * Get the port number of current running server
     * 
     * @return hostPort integer
     */
    private int getHostPort() {
        int hostPort = 0;
        // Get the hosting port using java
        MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
        ObjectName name;
        Server server = null;
        try {
            name = new ObjectName("Catalina", "type", "Server");
            server = (Server) mBeanServer.getAttribute(name, "managedResource");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Service[] services = server.findServices();
        for (Service service : services) {
            for (Connector connector : service.findConnectors()) {
                ProtocolHandler protocolHandler = connector.getProtocolHandler();
                if (protocolHandler instanceof Http11Protocol
                        || protocolHandler instanceof Http11AprProtocol
                        || protocolHandler instanceof Http11NioProtocol) {
                    System.out.println("HTTP Port: " + connector.getPort());
                    hostPort = connector.getPort();
                }
            }
        }
        return hostPort;
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
        host = (String) Hazelcast.getMap("subtile").get(
                Float.toString(subTileOriginX) + ":" + Float.toString(subTileOriginY));
        return host;
    }

    /**
     * Calculate (for storing purpose) coordination details of sub-tiles. Rationale :
     * Changing/moving of sub-tiles negligible with compared to game engine updating
     * 
     * @return ArrayList<SangraamaTile> about coordinations of sub-tiles
     */
    private ArrayList<SangraamaTile> calSubTilesCoordinations() {
        ArrayList<SangraamaTile> tiles = new ArrayList<>();
        Set<String> keySet = subtileMap.keySet();
        
        // Iterate though all keys
        for (String key : keySet) {
            // If sub-tile is inside current server, add to list
            if ( this.serverURL.equals(subtileMap.get(key)) ) {
                String[] s = key.split(":");
                tiles.add(new SangraamaTile(Float.parseFloat(s[0]), Float.parseFloat(s[1]),
                        this.subTileWidth, this.subTileHeight));
            }
        }
        if(D) System.out.println(TAG + " calculated size of tile (subtiles)");
        return tiles;
    }

    /**
     * Get details of sub-tiles coordinations
     * 
     * @return ArrayList<SangraamaTile> about coordinations of sub-tiles
     */
    public ArrayList<SangraamaTile> getSubTilesCoordinations() {
        this.tileInfo = this.calSubTilesCoordinations();
        return this.tileInfo;
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
