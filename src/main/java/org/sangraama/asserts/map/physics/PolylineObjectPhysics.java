package org.sangraama.asserts.map.physics;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *   The class containing physics related to polylines.
 */
public class PolylineObjectPhysics {

    private BodyDef bodyDef;       //object body definition - details needed to build the body.
    private FixtureDef fixtureDef;  //fixture of the object
    private ChainShape chainShape;    //   efficient way to connect many edges together
    private Vec2[] vector;

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

    /**
     *
     * @param x  coordinate
     * @param y  coordinate
     * define the body for the polyline object  and set the position.
     */
    public void createBodyDef(int x, int y) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;  // set the type of object to static
        bodyDef.position.set(x, y);   //set the x, y position of the object
    }

    /**
     *
     * @param vertices  - set of vertices of the object
     * @param count     - number of vertices
     *
     */
    public void createFixtureDef(Vec2[] vertices, int count) {
        chainShape = new ChainShape();
        // chainShape.createChain(vertices, count);   // This does not connect the first and last vertices.
        chainShape.createLoop(vertices, count);   //The first and last vertices are connected
        // chainShape.set(vertices, count);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;  //set the shape of the body to chain shape.
        fixtureDef.filter.groupIndex = 2;      //for collision purposes
        fixtureDef.userData = "island";     //a hook to link objects to bodies.
    }

}