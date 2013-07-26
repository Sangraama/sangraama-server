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
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private float x, y;
    private Vec2 velocity;
    private Body body;
    private BulletDelta bulletDelta;

    public Bullet(long playerId, float x, float y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.bodyDef = this.createBodyDef();
        this.fixtureDef = this.createFixDef();
        this.velocity = new Vec2(10f, 10f);
    }

    public BodyDef createBodyDef() {
        BodyDef bd = new BodyDef();
        bd.position.set(this.x, this.y);
        bd.type = BodyType.KINEMATIC;
        return bd;
    }

    public FixtureDef createFixDef() {
        CircleShape circle = new CircleShape();
        circle.m_radius = 0.25f;
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.friction = 0.2f;
        fd.restitution = 0.5f;
        return fd;
    }

    public void setBody(Body bulletBody) {
        this.body = bulletBody;
       
    }

    public Body getBody() {
        return this.body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public BulletDelta getBulletDelta() {
        bulletDelta = new BulletDelta(this.body.getPosition().x, this.body.getPosition().y,
                this.body.getAngle(), this.playerId);
        return bulletDelta;
    }

}
