package org.sangraama.assets;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.WebSocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * **************************************************************************
 * Inherited from Player class have the main functionality of a player in the game world.
 * It is also responsible for responding to client events. This does not have
 * body and fixture. Users of this class is suppose to implement those.
 *
 * @author : Gihan Karunarathne
 * @version : v1.2
 * @email : gckarunarathne@gmail.com
 * Date : 12/5/2013 4:00 PM
 * ***************************************************************************
 */
public class Ship extends Player {

    public static final Logger log = LoggerFactory.getLogger(Ship.class);

    /**
     * Create a ship
     *
     * @param userID     player user ID
     * @param x          x coordinate of the player
     * @param y          y coordinate of the player
     * @param w          width of AOI
     * @param h          height of AOI
     * @param health     current health of the player
     * @param score      current score of the player
     * @param con        web socket connection with client
     * @param imgType    player's physical view in client side
     * @param bulletType bullet's physical view in client side
     */
    public Ship(long userID, float x, float y, float w, float h, float health, float score,
                WebSocketConnection con, int imgType, int bulletType) {
        super(userID, x, y, w, h, health, score, con, imgType, bulletType);
    }

    public BodyDef getBodyDef() {
        BodyDef bd = new BodyDef();
        // log.info("create body def player x:" + this.x + " :" + this.y);
        bd.position.set(this.x, this.y);
        bd.type = BodyType.DYNAMIC;
        // bd.fixedRotation = true;
        return bd;
    }

    public FixtureDef getFixtureDef() {
        // CircleShape circle = new CircleShape();
        // circle.m_radius = 1f;
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(1.0f, 1f);

        FixtureDef fd = new FixtureDef();
        fd.density = 0.5f;
        // fd.shape = circle;
        fd.shape = ps;
        fd.friction = 0.2f;
        fd.restitution = 0.5f;
        fd.filter.groupIndex = 2;
        fd.userData = this;
        return fd;
    }
}
