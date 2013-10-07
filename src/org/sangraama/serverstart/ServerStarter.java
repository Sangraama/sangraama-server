package org.sangraama.serverstart;

import java.util.Properties;
import javax.servlet.ServletContextEvent;

import org.sangraama.assets.SangraamaMap;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.CollisionManager;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.gameLogic.UpdateEngine;
import org.sangraama.thrift.server.ThriftServer;

import com.hazelcast.core.Hazelcast;

public class ServerStarter implements javax.servlet.ServletContextListener {
    private ThriftServer thriftServer = null;
    private Thread gameEngine = null;
    private Thread updateEngine = null;
    private Thread collisionManager = null;
    private Thread thriftServerThread = null;
    private Properties prop;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        GameEngine.INSTANCE.setStop();
        CollisionManager.INSTANCE.setStop();
        UpdateEngine.INSTANCE.setStop();
        Hazelcast.shutdownAll();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        this.prop = new Properties();
        try {
            this.prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SangraamaMap.INSTANCE.setMap(Float.parseFloat(prop.getProperty("maporiginx")),
                Float.parseFloat(prop.getProperty("maporiginy")),
                Float.parseFloat(prop.getProperty("mapwidth")),
                Float.parseFloat(prop.getProperty("mapheight")), prop.getProperty("host") + ":"
                        + prop.getProperty("port") + "/" + prop.getProperty("dir")
                        + "/sangraama/player");
        System.out.println(prop.getProperty("host") + ":"
                + prop.getProperty("port") + "/" + prop.getProperty("dir")
                + "/sangraama/player");
        SangraamaMap.INSTANCE.setSubTileProperties(
                Float.parseFloat(prop.getProperty("subtilewidth")),
                Float.parseFloat(prop.getProperty("subtileheight")));
        this.updateEngine = new Thread(UpdateEngine.INSTANCE);
        this.updateEngine.start();
        
        this.gameEngine = new Thread(GameEngine.INSTANCE);
        this.gameEngine.start();
        this.collisionManager = new Thread(CollisionManager.INSTANCE);
        this.collisionManager.start();
        TileCoordinator.INSTANCE.generateSubtiles();
        TileCoordinator.INSTANCE.printEntriesInSubtileMap();
       
        // thriftServer = new ThriftServer(Integer.parseInt(prop.getProperty("thriftserverport")));
        // thriftServerThread = new Thread(thriftServer);
        // thriftServerThread.start();
        System.out.println("SANGRAAMA STARTED");
    }
}
