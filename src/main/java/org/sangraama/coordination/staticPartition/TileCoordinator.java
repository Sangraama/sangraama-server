package org.sangraama.coordination.staticPartition;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.nio.Address;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.coyote.http11.Http11Protocol;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.common.Constants;
import org.sangraama.jsonprotocols.send.SangraamaTile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public enum TileCoordinator {
    INSTANCE;
    private String TAG = "TileCoordinator: ";
    private Logger log = LoggerFactory.getLogger(TileCoordinator.class);

    private HazelcastInstance hazelcastInstance;

    Map<String, String> subtileMap;
    Map<String, Integer> playersCountByServerMap;
    Map<String, List<String>> subtilesByUrlMap;
    private float subTileHeight;
    private float subTileWidth;
    private SangraamaMap sangraamaMap;
    private String serverURL;
    private int serverPort = 8080;
    private List<SangraamaTile> tileInfo;

    TileCoordinator() {
        Config config = new Config();

       /* TcpIpConfig tcpIpConfig = config.getNetworkConfig().getJoin().getTcpIpConfig();
        tcpIpConfig.setEnabled(true);
        int port = 12301;
        config.setPortAutoIncrement(false);
        config.setPort(port);
        try{
            String host = "10.8.99.3";
            tcpIpConfig.addAddress(new Address(host + String.valueOf(0), port+1));
        for (int i = 0; i < 4; i++) {

            tcpIpConfig.addAddress(new Address(host + String.valueOf(i), port));
        }
        }catch (Exception e){
          e.printStackTrace();
        }*/

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        this.subtileMap = hazelcastInstance.getMap("subtile");
        this.playersCountByServerMap = hazelcastInstance.getMap("playersCountByServerMap");
        this.subtilesByUrlMap = hazelcastInstance.getMap("subtilesByUrlMap");
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.subTileHeight = sangraamaMap.getSubTileWidth();
        this.subTileWidth = sangraamaMap.getSubTileHeight();

        Properties prop = new Properties();
//        this.serverPort = this.getHostPort();
        try {
            prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
            this.serverURL = prop.getProperty("host") + ":" + this.serverPort + "/"
                    + prop.getProperty("dir") + "/sangraama/player";
            System.out.println(serverURL);
        } catch (Exception e) {
            log.error("sangraamaserver.properties file not found.");
            e.printStackTrace();
        }
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
                    log.info("HTTP Port: " + connector.getPort());
                    hostPort = connector.getPort();
                }
            }
        }
        return hostPort;
    }

    public void generateHazelcastMaps() {
        String subTileOrigins;
        List<String> subTileOriginList = new ArrayList<>();
        float subTileOriginX = 0, subTileOriginY = 0;
        Float originX = sangraamaMap.getOriginX();
        Float originY = sangraamaMap.getOriginY();
        for (int i = 0; i < sangraamaMap.getMapWidth() / subTileWidth; i++) {
            for (int j = 0; j < sangraamaMap.getMapHeight() / subTileHeight; j++) {
                if (originX != null) {
                    subTileOriginX = (i * subTileWidth) + originX;
                    subTileOriginY = (j * subTileHeight) + originY;
                    subTileOrigins = Float.toString(subTileOriginX) + ":"
                            + Float.toString(subTileOriginY);
                } else {
                    subTileOrigins = " : ";

                }
                subtileMap.put(subTileOrigins, serverURL);
                String[] result = serverURL.split(":");
                System.out.println(TAG + "host-" + result[0] + ", port-" + serverPort
                        + ", origin_x-" + subTileOriginX + ", origin_y-" + subTileOriginY);

                subTileOriginList.add(subTileOrigins);
            }
        }
        playersCountByServerMap.put(serverURL, 0);
        subtilesByUrlMap.put(serverURL, subTileOriginList);
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
        List<String> subTiles = getSubtilesInServer();
        ArrayList<SangraamaTile> tiles = new ArrayList<SangraamaTile>();

        // Iterate though all keys
        for (String subTile : subTiles) {
            // If sub-tile is inside current server, add to list
            String[] s = subTile.split(":");
            if (s[0] != null && !s[0].equals(" ")) {
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

    public Map<String, Integer> getPlayersCountByServerMap() {
        return playersCountByServerMap;
    }

    public String getServerURL() {
        return this.serverURL;
    }

    public List<String> getSubtilesInServer() {
        return subtilesByUrlMap.get(serverURL);
    }

    public Map<String, List<String>> getSubtilesInServerMap() {
        return subtilesByUrlMap;
    }

    public Map<String, String> getSubtileMap() {
        return subtileMap;
    }


}
