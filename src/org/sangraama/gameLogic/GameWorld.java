package org.sangraama.gameLogic;

import java.util.ArrayList;
import java.util.Collection;

import org.jbox2d.dynamics.World;
import org.sangraama.asserts.Player;
import org.sangraama.common.Constants;

public class GameWorld implements Runnable{
	private static GameWorld gameWorld=null;
	public int i=0;
  public World world;
  private boolean execute=true;
  private ArrayList<JBoxPlayer> players = null;
  private int WIDTH=600;
  private int HEIGHT=600;
  
  public GameWorld() {
		  world = new World(Constants.gravity, Constants.doSleep);
		  players = new ArrayList<JBoxPlayer>();
  }
  
  public static GameWorld getInstance(){
	  if(gameWorld==null){
		  gameWorld=new GameWorld();
	  }
	 return gameWorld;
	  
  }

  public void init() {
	  
  }

  public void seti(){
	 i++;
  }
  
  
  public void update() {
	 
  }

  public void pushUpdate() {

//	  if(players!=null){
//		  System.out.println("Player X :"+players.get(0).getPosX()+", Player Y :"+players.get(0).getPosY());
//	  
//	  }
  }
  
  public void addPlayer(int id){
	  players.add(new JBoxPlayer(id, 10, 10, 5));
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

@Override
public void run() {
	init();
    while (execute) {
      update();
      world.step(Constants.timeStep, Constants.velocityIterations,Constants.positionIterations);
      System.out.println("aaaaaaaaaaaaaaa");
      pushUpdate();
    }
}
  
	public void stopSimulating(){
		execute=false;
	}
}
