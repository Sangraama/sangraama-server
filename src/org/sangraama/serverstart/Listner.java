package org.sangraama.serverstart;

import javax.servlet.ServletContextEvent;

import org.sangraaama.thrift.server.ThriftServer;
import org.sangraama.gameLogic.GameEngine;

public class Listner implements javax.servlet.ServletContextListener {
	Thread t;
	 ThriftServer ts=null;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ts.stopServer();	
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("SANGRAAMA STARTED");
		GameEngine engine = GameEngine.INSTANCE;
		Thread t = new Thread(engine);
		t.start();
		
		 ts=new ThriftServer();
         t = new Thread(ts);
          t.start();
	}
}
