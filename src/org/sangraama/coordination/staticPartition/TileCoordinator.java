package org.sangraama.coordination.staticPartition;

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

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public enum TileCoordinator {
    INSTANCE;
    private String TAG = "TileCoordinator : ";

    private HazelcastInstance hz;
    Map<String, String> subtileMap;
    private float subTileHeight;
    private float subTileWidth;
    private SangraamaMap sangraamaMap;
    private String serverURL;

    TileCoordinator() {
        Config cfg = new Config();
        this.hz = Hazelcast.newHazelcastInstance(cfg);
        this.subtileMap = hz.getMap("subtile");
        this.sangraamaMap = SangraamaMap.INSTANCE;
        this.subTileHeight = sangraamaMap.getSubTileWidth();
        this.subTileWidth = sangraamaMap.getSubTileHeight();
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverURL = prop.getProperty("host") +":"+ prop.getProperty("port") +"/"+ prop.getProperty("dir")+"/sangraama/player";
        System.out.println(TAG + serverURL);

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
                }
            }
        }
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

    public String getSubTileHost(float oriX, float oriY) {
        String host = "";
        host = (String) hz.getMap("subtile")
                .get(Float.toString(oriX) + ":"
                        + Float.toString(oriY));
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
