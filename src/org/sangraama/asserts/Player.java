package org.sangraama.asserts;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.jbox2d.dynamics.World;
import org.sangraama.common.Constants;
import org.sangraama.controller.EventHandler;
import org.sangraama.gameLogic.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    private int userID = 0;
    private int x = 0, y = 0;
    private BodyDef bodyDef = null;
    private FixtureDef fixtureDef = null;
    private Body body = null;
    // private static LoggerFactory.getLogger Logger log = (EventHandler. class
    // );
    private static Logger log = LoggerFactory.getLogger(EventHandler.class);
    GameEngine sangraamaWorld = GameEngine.getInstance();

    public Player(int userID) {
	this.bodyDef = createBodyDef();
	this.fixtureDef = createFixtureDef();
	this.userID = userID;
    }

    private BodyDef createBodyDef() {
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

    public int getUserID() {
	return this.userID;
    }

}
