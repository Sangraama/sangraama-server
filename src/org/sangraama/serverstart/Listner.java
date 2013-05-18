package org.sangraama.serverstart;

import javax.servlet.ServletContextEvent;

import org.sangraama.gameLogic.GameEngine;

public class Listner implements javax.servlet.ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("SANGRAAMA STARTED");
		GameEngine engine = GameEngine.INSTANCE;
		Thread t = new Thread(engine);
		t.start();
	}
}
