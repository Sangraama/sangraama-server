package org.sangraama.serverstart;

import com.hazelcast.core.Hazelcast;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.coyote.http11.Http11Protocol;
import org.sangraama.coordination.SangraamaMap;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.aoi.AOIEngine;
import org.sangraama.gameLogic.CollisionManager;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.server.ThriftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Properties;

public class ServerStarter implements ServletContextListener {
    private Logger log = LoggerFactory.getLogger(ServerStarter.class);
    private ThriftServer thriftServer = null;
    private Thread gameEngine = null;
    private Thread aoiEngine = null;
    private Thread collisionManager = null;
    private Thread thriftServerThread = null;
    private Properties prop;
    public static ServletContext context;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        GameEngine.INSTANCE.setStop();
        CollisionManager.INSTANCE.setStop();
        AOIEngine.INSTANCE.setStop();
        Hazelcast.shutdownAll();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Sangraama Server Starting ... ");
        this.prop = new Properties();
        try {
            this.prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
            int serverPort = this.getHostPort();
            SangraamaMap.INSTANCE.setMap(Float.parseFloat(prop.getProperty("maporiginx")),
                    Float.parseFloat(prop.getProperty("maporiginy")),
                    Float.parseFloat(prop.getProperty("mapwidth")),
                    Float.parseFloat(prop.getProperty("mapheight")), prop.getProperty("host") + ":"
                    + serverPort + "/" + prop.getProperty("dir")
                    + "/sangraama/player", Float.parseFloat(prop.getProperty("maxlength")),
                    Float.parseFloat(prop.getProperty("maxheight")));

            SangraamaMap.INSTANCE.setSubTileProperties(
                    Float.parseFloat(prop.getProperty("subtilewidth")),
                    Float.parseFloat(prop.getProperty("subtileheight")));
            this.aoiEngine = new Thread(AOIEngine.INSTANCE);
            this.aoiEngine.start();
            this.gameEngine = new Thread(GameEngine.INSTANCE);
            this.gameEngine.start();
            this.collisionManager = new Thread(CollisionManager.INSTANCE);
            this.collisionManager.start();
            TileCoordinator.INSTANCE.init();// Should initialized after "SangraamaMap"
            TileCoordinator.INSTANCE.generateSubTiles();

            // thriftServer = new ThriftServer(Integer.parseInt(prop.getProperty("thriftserverport")));
            // thriftServerThread = new Thread(thriftServer);
            // thriftServerThread.start();
            log.info("SANGRAAMA STARTED");
            System.out.println("Sangraama Server Started ... ");
        } catch (Exception e) {
            log.error("Server can't start. Error occured while reading property file {}", e);
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

}
