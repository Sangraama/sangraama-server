package org.sangraama.serverstart;

import javax.servlet.ServletContextEvent;

import org.sangraama.gameLogic.GameEngine;

public class Listner implements javax.servlet.ServletContextListener {
    	//GameEngine gameEngine = GameEngine.getInstance();
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("SANGRAAMA STARTED");
		
	}

}
