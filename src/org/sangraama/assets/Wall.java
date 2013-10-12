package org.sangraama.assets;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class Wall {

    private BodyDef wallDef;
    private FixtureDef wallFixture;
    private float x;
    private float y;
    private float width;
    private float height;

    public Wall(float x, float y, float width, float height) {
        this.width = width;
        this.height= height;
        this.x = x;
        this.y = y;
    }

    public BodyDef getBodyDef() {
        wallDef = new BodyDef();
        wallDef.position.set(x, y);
        wallDef.type = BodyType.STATIC;
        return wallDef;
    }
    
    public FixtureDef getFixtureDef(){
        PolygonShape wallShape = new PolygonShape();
        wallShape.setAsBox(width, height);
        wallFixture = new FixtureDef();
        wallFixture.shape = wallShape;
        wallFixture.userData = "wall";
        return wallFixture;
    }
}
