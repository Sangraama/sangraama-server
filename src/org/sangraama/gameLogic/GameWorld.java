package org.sangraama.gameLogic;

import java.util.ArrayList;
import java.util.Collection;

import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public class GameWorld {
  static World world;
  private ArrayList<Player> players = null;
  private int WIDTH=600;
  private int HEIGHT=600;
  
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
  
//Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
  public float toPixelPosX(float posX) {
      float x = WIDTH*posX / 100.0f;
      return x;
  }
   
  //Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
  public float toPosX(float posX) {
      float x =   (posX*100.0f*1.0f)/WIDTH;
      return x;
  }
   
  //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
  public float toPixelPosY(float posY) {
      float y = HEIGHT - (1.0f*HEIGHT) * posY / 100.0f;
      return y;
  }
   
  //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
  public float toPosY(float posY) {
      float y = 100.0f - ((posY * 100*1.0f) /HEIGHT) ;
      return y;
  }
   
  //Convert a JBox2D width to pixel width
  public float toPixelWidth(float width) {
      return WIDTH*width / 100.0f;
  }
   
  //Convert a JBox2D height to pixel height
  public  float toPixelHeight(float height) {
      return HEIGHT*height/100.0f;
  }
  
}
