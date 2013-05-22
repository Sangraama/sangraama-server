package org.sangraama.serverstart;

import javax.servlet.ServletContextEvent;

import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.server.ThriftServer;

public class Listner implements javax.servlet.ServletContextListener {
    private ThriftServer thriftServer = null;
    private Thread gameEngine = null;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
	thriftServer.stopServer();
	GameEngine.INSTANCE.stopGameWorld();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
	System.out.println("SANGRAAMA STARTED");
	this.gameEngine = new Thread(GameEngine.INSTANCE);
	this.gameEngine.start();

	// thriftServer = new ThriftServer();
	// thriftServer.start();
    }
}
