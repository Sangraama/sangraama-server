package org.sangraama.asserts;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.gameLogic.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    // Debug
 // Local Debug or logs
    private static boolean LL = true;
    private static boolean LD = true;
    public static final Logger log = LoggerFactory.getLogger(Player.class);
    private static final String TAG = "player :";
    
    private int userID = 0;

    private BodyDef bodyDef = null;
    private FixtureDef fixtureDef = null;
    private Body body = null;
    private GameEngine gameEngine = null;
    // WebSocket Connection
    private WebSocketConnection con = null;

    // Player Dynamic Parameters
    private int x = 0, y = 0;
    public float v_x = 0, v_y = 0;
    private Vec2 v = new Vec2(0f, 0f);

    public Player() {
    }
    
    
    
    public Player(int userID,WebSocketConnection con) {
	System.out.println(TAG + "init player");
	this.bodyDef = this.createBodyDef();
	this.fixtureDef = createFixtureDef();
	this.userID = userID;
	this.con = con;
	this.gameEngine = GameEngine.INSTANCE;
	this.gameEngine.addToPlayerQueue(this);
    }

    public void sendUpdate() {
	// con.sendUpdate(this);
	System.out.println(TAG + " x:"+ this.body.getPosition().x +" "+ "y:"+this.body.getPosition().y);
    }
    
    public void applyUpdate(){
	this.body.setLinearVelocity(this.getV());
    }

    BodyDef createBodyDef() {
	BodyDef bd = new BodyDef();
	bd.position.set(50, 50);
	bd.type = BodyType.DYNAMIC;
	return bd;
    }

    public BodyDef getBodyDef() {
	return this.bodyDef;
    }

    private FixtureDef createFixtureDef() {
	CircleShape circle = new CircleShape();
	circle.m_radius = 1f;

	FixtureDef fd = new FixtureDef();
	fd.density = 0.5f;
	fd.shape = circle;
	fd.friction = 0.2f;
	fd.restitution = 0.5f;
	return fd;
    }

    public FixtureDef getFixtureDef() {
	return this.fixtureDef;
    }

    public void setBody(Body body) {
	this.body = body;
    }

    public Body getBody() {
	return this.body;
    }

    public void setX(int x) {
	if (x > 0) {
	    this.x = x;
	}
    }

    public int getX() {
	return x;
    }

    public void setY(int y) {
	if (y > 0) {
	    this.y = y;
	}
    }

    public int getY() {
	return this.y;
    }
    
    public Vec2 getV(){
	return this.v;
    }
    
    public void setV(float x , float y) {
	this.v.set(x, y);
	//System.out.println(TAG + " set V :");
    }

    public int getUserID() {
	return this.userID;
    }

}
