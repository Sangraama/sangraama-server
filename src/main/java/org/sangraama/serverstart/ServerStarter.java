package org.sangraama.serverstart;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.sangraama.assets.SangraamaMap;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.CollisionManager;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.gameLogic.AOIEngine;
import org.sangraama.thrift.server.ThriftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextListener;

import com.hazelcast.core.Hazelcast;

public class ServerStarter implements ServletContextListener {
    private Logger log = LoggerFactory.getLogger(ServerStarter.class);
    private ThriftServer thriftServer = null;
    private Thread gameEngine = null;
    private Thread updateEngine = null;
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
        } catch (Exception e) {
            log.error("Error occured while reading property file {}", e);
        }
        SangraamaMap.INSTANCE.setMap(Float.parseFloat(prop.getProperty("maporiginx")),
                Float.parseFloat(prop.getProperty("maporiginy")),
                Float.parseFloat(prop.getProperty("mapwidth")),
                Float.parseFloat(prop.getProperty("mapheight")), prop.getProperty("host") + ":"
                        + prop.getProperty("port") + "/" + prop.getProperty("dir")
                        + "/sangraama/player", Float.parseFloat(prop.getProperty("maxlength")),
                Float.parseFloat(prop.getProperty("maxheight")));

        SangraamaMap.INSTANCE.setSubTileProperties(
                Float.parseFloat(prop.getProperty("subtilewidth")),
                Float.parseFloat(prop.getProperty("subtileheight")));
        this.updateEngine = new Thread(AOIEngine.INSTANCE);
        this.updateEngine.start();
        this.gameEngine = new Thread(GameEngine.INSTANCE);
        this.gameEngine.start();
        this.collisionManager = new Thread(CollisionManager.INSTANCE);
        this.collisionManager.start();
        TileCoordinator.INSTANCE.generateSubtiles();

        // thriftServer = new ThriftServer(Integer.parseInt(prop.getProperty("thriftserverport")));
        // thriftServerThread = new Thread(thriftServer);
        // thriftServerThread.start();
        log.info("SANGRAAMA STARTED");
        System.out.println("Sangraama Server Started ... ");
    }
}
