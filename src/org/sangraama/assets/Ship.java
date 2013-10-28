package org.sangraama.assets;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.common.Constants;
import org.sangraama.controller.WebSocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ship extends Player {

    // Debug
    // Local Debug or logs
    public static final Logger log = LoggerFactory.getLogger(Ship.class);
    private static final String TAG = "Ship : ";

    public Ship(long userID, float x, float y, float w, float h, float health, float score,
            WebSocketConnection con) {
        super(userID, x, y, w, h, health, score, con);
    }

    public BodyDef getBodyDef() {
        BodyDef bd = new BodyDef();
        System.out.println(TAG + "create body def player x:" + this.x + " :" + this.y);
        bd.position.set(this.x, this.y);
        bd.type = BodyType.DYNAMIC;
        // bd.fixedRotation = true;
        return bd;
    }

    public FixtureDef getFixtureDef() {
        // CircleShape circle = new CircleShape();
        // circle.m_radius = 1f;
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(1f, 1f);

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
