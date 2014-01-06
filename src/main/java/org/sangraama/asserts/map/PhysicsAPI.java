package org.sangraama.asserts.map;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.sangraama.asserts.map.physics.PolygonObjectPhysics;
import org.sangraama.asserts.map.physics.PolylineObjectPhysics;
import org.sangraama.common.Constants;

import java.util.List;

public class PhysicsAPI {
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public void applyPhysics(StaticObject staticObject) {

        if (staticObject.getType().equals("polygon")) {
            PolygonObjectPhysics polygon = new PolygonObjectPhysics();
            polygon.createBodyDef(staticObject.getX(), staticObject.getY());
            this.bodyDef = polygon.getBodyDef();
            Vec2[] vertices = new Vec2[staticObject.getCoordinates().size()];
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = new Vec2();
                vertices[i].set(staticObject.getCoordinates().get(i).getX(), staticObject.getCoordinates().get(i).getY());
            }

            polygon.createFixtureDef(vertices, vertices.length);
            this.fixtureDef = polygon.getFixtureDef();
        }
        if (staticObject.getType().equals("polyline")) {
            PolylineObjectPhysics polyline = new PolylineObjectPhysics();
            int x = (int) scale(staticObject.getX());
            int y = (int) scale(staticObject.getY());
            polyline.createBodyDef(x, y);
            this.bodyDef = polyline.getBodyDef();
            Vec2[] vertices = new Vec2[staticObject.getCoordinates().size()];
            for (int i = 0; i < vertices.length; i++) {

                vertices[i] = new Vec2();
                float verticeX = scale(staticObject.getCoordinates().get(i).getX());
                float verticeY = scale(staticObject.getCoordinates().get(i).getY());
                vertices[i].set(verticeX, verticeY);

            }
            polyline.createFixtureDef(vertices, vertices.length);

            this.fixtureDef = polyline.getFixtureDef();


        }
    }

    public void applyPhysics(List<StaticObject> staticObjects, World world) {
        for (StaticObject staticObject : staticObjects) { // for each static object
            applyPhysics(staticObject); //apply the physics to that object.
            Body newStaticObjectBody = world.createBody(this.getBodyDef()); // add the static object to the game world.
            newStaticObjectBody.createFixture(this.getFixtureDef());


        }
    }

    public float scale(int unitToBeScaled) {
        return unitToBeScaled / Constants.scale;
    }

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

}
