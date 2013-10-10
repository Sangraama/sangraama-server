package org.sangraama.assets;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.sangraama.controller.clientprotocol.BulletDelta;

public class Bullet {

    private String TAG = "Bullet : ";
    private long playerId;

    private float x, y;
    private Vec2 velocity;
    private int v_rate = 300;
    private Body body;
    private BulletDelta bulletDelta;
    private long id;

    public Bullet(long id, long playerId, float x, float y) {
        this.id = id;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.velocity = new Vec2(1.0f * v_rate, 1.0f * v_rate);
    }

    public void setBody(Body bulletBody) {
        this.body = bulletBody;

    }

    public Body getBody() {
        return this.body;
    }

    public BodyDef getBodyDef() {
        BodyDef bd = new BodyDef();
        bd.position.set(this.x, this.y);
        bd.type = BodyType.DYNAMIC;
        bd.bullet = true;
        // bd.fixedRotation = true;
        return bd;
    }

    public FixtureDef getFixtureDef() {
        CircleShape circle = new CircleShape();
        circle.m_radius = 1.0f;
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = 0.1f;
        // fd.shape = circle;
        //fd.friction = 0.2f;
        //fd.restitution = 0.5f;
        fd.filter.groupIndex = 2;
        return fd;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public long getId() {
        return id;
    }

    public long getPlayerId() {
        return this.playerId;
    }

    public BulletDelta getBulletDelta(int type) {
        bulletDelta = new BulletDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.playerId, this.id, type);
        return bulletDelta;
    }

}
