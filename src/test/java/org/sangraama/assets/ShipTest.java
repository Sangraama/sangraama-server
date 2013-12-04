package org.sangraama.assets;

import junit.framework.Assert;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class Description.
 *
 * @author: Gihan Karunarathne
 * Date: 12/5/13
 * Time: 4:44 AM
 * @email: gckarunarathne@gmail.com
 */
public class ShipTest {
    private Ship ship;
    float x = 1000, y = 1000;

    @Before
    public void setUp() throws Exception {
        ship = new Ship(1, x, y, 600, 400, 100, 5000, null, 1, 1);
    }

    @After
    public void tearDown() throws Exception {
        ship = null;
    }

    @Test
    public void testGetBodyDef() throws Exception {
        BodyDef bd = new BodyDef();
        bd.position.set(this.x, this.y);
        bd.type = BodyType.DYNAMIC;

        Assert.assertNotNull(ship.getBodyDef());
    }

    @Test
    public void testGetFixtureDef() throws Exception {
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

        Assert.assertNotNull(ship.getFixtureDef());
    }
}
