package org.sangraama.gameLogic;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class JBoxPlayer {
	BodyDef playerBodyDef;
	Body playerBody;
	GameWorld gameWorld=GameWorld.getInstance();
	private float posX;
	private float posY;
	private float size;
	private int id;
	
	public JBoxPlayer(int id,float posX,float posY,float size){
		this.posX=posX;
		this.posY=posY;
		this.size=size;
		this.id=id;
		create();
	}
	
	public void create(){
		
		playerBodyDef=new BodyDef();
		playerBodyDef.type=BodyType.DYNAMIC;
		playerBodyDef.position.set(posX,posY);
		
		PolygonShape ps=new PolygonShape();
		ps.setAsBox(size,size);
		
		FixtureDef playerFixtureDef=new FixtureDef();
		playerFixtureDef.shape=ps;
		playerFixtureDef.friction=1.0f;
		
		playerBody=gameWorld.world.createBody(playerBodyDef);
		playerBody.createFixture(playerFixtureDef);
	}
	
	public float getPosX() {
		Vec2 v=playerBody.getPosition();
		posX=v.x;
		return posX;
	}
	private void setPosX(float posX) {
		this.posX = posX;
	}
	public float getPosY() {
		Vec2 v=playerBody.getPosition();
		posY=v.y;
		return posY;
	}
	private void setPosY(float posY) {
		this.posY = posY;
	}

	private int getId() {
		return id;
	}

}
