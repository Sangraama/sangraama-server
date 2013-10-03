package org.sangraama.assets;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.common.Constants;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ship extends Player {

    // Debug
    // Local Debug or logs
    public static final Logger log = LoggerFactory.getLogger(Ship.class);
    private static final String TAG = "Ship : ";

    public Ship(long userID, WebSocketConnection con) {
        super(userID, con);
    }

    public Ship(long userID, float x, float y, float w, float h, WebSocketConnection con) {
        super(userID, x, y, w, h, con);
    }

    public PlayerDelta getPlayerDelta() {
        // if (!isUpdate) {
        if ((this.body.getPosition().x - this.x) != 0f || (this.body.getPosition().y - this.y) != 0) {
            System.out.println(TAG + "id : " + this.userID + " x:" + x + " " + "y:" + y + " angle:"
                    + this.body.getAngle() + "&" + this.body.getAngularVelocity());
        }

        // this.delta = new PlayerDelta(this.body.getPosition().x - this.x,
        // this.body.getPosition().y - this.y, this.userID);
        this.delta = new PlayerDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.userID);
        for (Bullet bullet : this.bulletList) {
            delta.getBulletDeltaList().add(bullet.getBulletDelta(1));
        }
        for (Bullet bullet : this.removedBulletList) {
            delta.getBulletDeltaList().add(bullet.getBulletDelta(2));
        }
        this.x = this.body.getPosition().x;
        this.y = this.body.getPosition().y;
        this.angle = this.body.getAngle();
        // Check whether player is inside the tile or not
        /*
         * Gave this responsibility to client if (!this.isInsideMap(this.x, this.y)) {
         * PlayerPassHandler.INSTANCE.setPassPlayer(this); }
         */

        // isUpdate = true;
        // }
        if (!isInsideServerSubTile(this.x, this.y)) {
            PlayerPassHandler.INSTANCE.setPassPlayer(this);
            System.out.println(TAG + "outside of the subtile detected");
        }
        return this.delta;
    }

    public void shoot(float s) {
        float r = 50;
        if (s == 1) {
            float x = this.body.getPosition().x;
            float y = this.body.getPosition().y;
            if (0 <= this.angle && this.angle <= 90) {
                float ang = this.angle * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));
                x = x + rX;
                y = y + rY;
            } else if (90 <= this.angle && this.angle <= 180) {
                float ang = (180 - this.angle) * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));

                x = x - rX;
                y = y + rY;
            } else if (180 <= this.angle && this.angle <= 270) {
                float ang = (this.angle - 180) * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));

                x = x - rX;
                y = y - rY;
            } else if (270 <= this.angle && this.angle <= 360) {
                float ang = (360 - this.angle) * Constants.TO_RADIANS;
                float rX = (float) (r * Math.cos(ang));
                float rY = (float) (r * Math.sin(ang));

                x = x + rX;
                y = y - rY;
            }
            long id = (long) (Math.random() * 10000);
            Bullet bullet = new Bullet(id, this.userID, x, y);
            this.newBulletList.add(bullet);
            System.out.println(TAG + ": Added a new bullet");
        }
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
        ps.setAsBox(10f, 10f);

        FixtureDef fd = new FixtureDef();
        fd.density = 0.5f;
        // fd.shape = circle;
        fd.shape = ps;
        fd.friction = 0.2f;
        fd.restitution = 0.5f;
        fd.filter.groupIndex = 2;
        return fd;
    }
}
