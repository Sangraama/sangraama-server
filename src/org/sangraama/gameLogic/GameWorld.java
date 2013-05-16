package org.sangraama.gameLogic;

import java.util.ArrayList;

import org.jbox2d.dynamics.World;
import org.sangraama.common.Constants;
import org.sangraama.controller.PlayerServlet;

public class GameWorld {
    private World world;
    private ArrayList<PlayerServlet> players = null;

    public GameWorld() {
	this.world = new World(Constants.gravity, Constants.doSleep);
	this.players = new ArrayList<PlayerServlet>();
    }

    public void init() {
	PlayerServlet p = new PlayerServlet();
	p.getPlayerDef().setBody(
		world.createBody(p.getPlayerDef().getBodyDef()));
	p.getPlayerDef().getBody()
		.createFixture(p.getPlayerDef().getFixtureDef());
	players.add(new PlayerServlet());
    }

    public void run() {
	init();
	while (true) {
	    update();
	    world.step(Constants.timeStep, Constants.velocityIterations,
		    Constants.positionIterations);
	    pushUpdate();
	}
    }

    public void update() {

    }

    public void pushUpdate() {

    }

    public void addPlayerQueue() {

    }
}
