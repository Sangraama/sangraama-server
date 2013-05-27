package org.sangraama.asserts;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.PlayerConnectionHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.coordination.ClientTransferReq;
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
	private SangraamaMap sangraamaMap = null;
	// WebSocket Connection
	private WebSocketConnection con = null;
	private boolean isUpdate = false;

	// Player Dynamic Parameters
	private float x = 0, y = 0;
	public float v_x = 0, v_y = 0;
	private Vec2 v = new Vec2(0f, 0f);
	private PlayerDelta delta = null;

	public boolean isUpdate() {
		return this.isUpdate;
	}

	public Player(int userID, WebSocketConnection con) {
		System.out.println(TAG + "init player");
		this.bodyDef = this.createBodyDef();
		this.fixtureDef = createFixtureDef();
		this.userID = userID;
		this.con = con;
		this.gameEngine = GameEngine.INSTANCE;
		this.gameEngine.addToPlayerQueue(this);
		this.sangraamaMap = SangraamaMap.INSTANCE;
	}

	/**
	 * 
	 * @return null
	 */
	public PlayerDelta getPlayerDelta() {
		// if (!isUpdate) {
		System.out.println(TAG + "id: " + this.userID + " x:"
				+ this.body.getPosition().x + " " + "y:"
				+ this.body.getPosition().y);

		this.delta = new PlayerDelta(this.body.getPosition().x - this.x,
				this.body.getPosition().y - this.y, this.userID);
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		// Check whether player is inside the tile or not
		if (!this.isInsideMap(this.x, this.y)) {
			PlayerConnectionHandler.INSTANCE.setPassPlayer(this);
		}

		// isUpdate = true;
		// }
		return this.delta;
	}

	public void sendUpdate(ArrayList<PlayerDelta> deltaList) {
		con.sendUpdate(deltaList);
		// isUpdate = false;
	}

	public void applyUpdate() {
		this.body.setLinearVelocity(this.getV());
	}

	private boolean isInsideMap(float x, float y) {
		if (0 <= x && x < sangraamaMap.getMapWidth() && 0 <= y
				&& y <= sangraamaMap.getMapHeight()) {
			return true;
		} else {
			return false;
		}
	}

	public void sendNewConnection(ClientTransferReq transferReq) {
		con.sendNewConnection(transferReq);
	}

	BodyDef createBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(995, 50);
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

	public void setX(float x) {
		if (x > 0) {
			this.x = x;
		}
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		if (y > 0) {
			this.y = y;
		}
	}

	public float getY() {
		return this.y;
	}

	public Vec2 getV() {
		return this.v;
	}

	public void setV(float x, float y) {
		this.v.set(x, y);
		// System.out.println(TAG + " set V :");
	}

	public int getUserID() {
		return this.userID;
	}
}
