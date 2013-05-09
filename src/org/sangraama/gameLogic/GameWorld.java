package org.sangraama.gameLogic;

import java.util.ArrayList;
import java.util.Collection;

import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public class GameWorld {
  private World world;
  private ArrayList<Player> players = null;

  public GameWorld() {
    this.world = new World(Constants.gravity, Constants.doSleep);
    this.players = new ArrayList<Player>();
  }

  public void init() {
    Player p = new Player();
    p.getPlayerDef().setBody( world.createBody(p.getPlayerDef().getBodyDef()) );
    p.getPlayerDef().getBody().createFixture(p.getPlayerDef().getFixtureDef());
    players.add(new Player());
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
}
