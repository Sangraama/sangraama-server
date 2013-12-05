package org.sangraama.asserts.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: denuwanthi
 * Date: 12/4/13
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMapAPI {
    private int mapHeight;
    private int mapWidth;
    private List<StaticObject> staticObjects = new ArrayList<>();
    private int mapHeightFromFile=30016;
    private int mapWidthFromFile=30016;
    private  int numofObjectsFromFile=396;
    @Before
    public void setUp(){
      MapAPI mapAPI = new MapAPI();
      mapAPI.getMapDetails();
      mapHeight=mapAPI.getMapHeight();
      mapWidth=mapAPI.getMapWidth();
      staticObjects=mapAPI.getStaticObjects();

    }

    @Test
    public void testFunctionMapHeight_testCase1() {
        Assert.assertNotNull(mapHeight);
        Assert.assertEquals(mapHeightFromFile,mapHeight);
    }

    @Test
    public void testFunctionMapWidth_testCase2() {
        Assert.assertNotNull(mapWidth);
        Assert.assertEquals(mapWidthFromFile,mapWidth);
    }

    @Test
    public void testFunctionStaticObjects_testCase3() {
        Assert.assertNotNull(staticObjects);
        Assert.assertEquals(numofObjectsFromFile,staticObjects.size());
    }
}
