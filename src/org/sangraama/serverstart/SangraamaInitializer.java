package org.sangraama.serverstart;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.sangraama.asserts.SangraamaMap;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.gameLogic.PassedPlayer;
import org.sangraama.thrift.server.ThriftServer;

public class SangraamaInitializer implements javax.servlet.ServletContextListener {
    private ThriftServer thriftServer = null;
    private Thread gameEngine = null;
    private Thread thriftServerThread = null;
    private Properties prop;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        thriftServer.stopServer();
        GameEngine.INSTANCE.stopGameWorld();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        try {
            this.prop = new Properties();
            this.prop.load(getClass().getResourceAsStream("/sangraamaserver.properties"));
            SangraamaMap.INSTANCE.setMap(Float.parseFloat(prop.getProperty("maporiginx")),
                    Float.parseFloat(prop.getProperty("maporiginy")),
                    Float.parseFloat(prop.getProperty("mapwidth")),
                    Float.parseFloat(prop.getProperty("mapheight")));
            this.gameEngine = new Thread(GameEngine.INSTANCE);
            this.gameEngine.start();
            thriftServer = new ThriftServer(Integer.parseInt(prop.getProperty("thriftserverport")));
            thriftServerThread = new Thread(thriftServer);
            thriftServerThread.start();
            System.out.println("SANGRAAMA STARTED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
