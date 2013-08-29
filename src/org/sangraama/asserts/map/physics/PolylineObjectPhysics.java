package org.sangraama.asserts.map.physics;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PolylineObjectPhysics {
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    private ChainShape chainShape;
    private Vec2[] vector;

    public ChainShape getChainShape() {
        return chainShape;
    }

    public void setChainShape(ChainShape chainShape) {
        this.chainShape = chainShape;
    }

    public Vec2[] getVector() {
        return vector;
    }

    public void setVector(Vec2[] vector) {
        this.vector = vector;

    }

    public void createChain(Vec2[] vertices, int count) {
        chainShape = new ChainShape();
        chainShape.createChain(vertices, count);
        chainShape.createLoop(vertices, count);
    }

    public void createBodyDef(int x, int y) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(x, y);
    }

    public void createFixtureDef(Vec2[] vertices, int count) {
        chainShape = new ChainShape();
        // chainShape.createChain(vertices, count);
        // System.out.println("Chain created");
        chainShape.createLoop(vertices, count);
        // chainShape.set(vertices, count);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.userData = "Island";
    }
}
