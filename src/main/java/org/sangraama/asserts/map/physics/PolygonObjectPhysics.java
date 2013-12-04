package org.sangraama.asserts.map.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *   The class containing physics related to polygons.
 */
public class PolygonObjectPhysics {
    private BodyDef bodyDef;          //object body definition - details needed to build the body.
    private FixtureDef fixtureDef;           //fixture of the object
    private PolygonShape polygonShape;         // polygon shapes

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

    public PolygonShape getPolygonShape() {
        return polygonShape;
    }

    public void setPolygonShape(PolygonShape polygonShape) {
        this.polygonShape = polygonShape;
    }


    /**
     *
     * @param x  coordinate
     * @param y  coordinate
     * define the body for the polygon object  and set the position.
     */
    public void createBodyDef(int x, int y) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(x, y);
    }
    /**
     *
     * @param vertices  - set of vertices of the object
     * @param count     - number of vertices
     */
    public void createFixtureDef(Vec2[] vertices, int count) {
        polygonShape = new PolygonShape();

        polygonShape.set(vertices, count);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;     //set the shape of the body to polygon shape
        fixtureDef.filter.groupIndex = 2;      //for collision purposes
        fixtureDef.userData = "island"; //a hook to link objects to bodies.

    }
}