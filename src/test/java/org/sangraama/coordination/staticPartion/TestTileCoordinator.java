package org.sangraama.coordination.staticPartion;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.common.Constants;
import org.sangraama.coordination.staticPartition.TileCoordinator;

import com.hazelcast.core.HazelcastInstance;

public class TestTileCoordinator {

    private HazelcastInstance hazelcastInstance;
    Map<String, String> subtileMap;

    @Before
    public void setup() {
        SangraamaMap.INSTANCE.setMap(0, 0, 4000, 4000, "localhost:8080/sangraama", 8000, 8000);
        SangraamaMap.INSTANCE.setSubTileProperties(2000, 2000);
        TileCoordinator.INSTANCE.init();
        TileCoordinator.INSTANCE.generateSubtiles();
        hazelcastInstance = TileCoordinator.INSTANCE.getHazelcastInstance();
        subtileMap = hazelcastInstance.getMap("subtile");
    }

    @Test
    public void testFunctionGetHazelcastInstance_testCase1() {
        Assert.assertTrue(hazelcastInstance != null);
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase1() {
        Assert.assertTrue(subtileMap.size() > 0);
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase2() {
        Assert.assertTrue(subtileMap.size() == 4);
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase3() {
        Assert.assertTrue(subtileMap.containsValue("localhost:8080/sangraama"));
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase4() {
        for (String host : subtileMap.values()) {
            Assert.assertTrue("localhost:8080/sangraama".equals(host));
        }
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase5() {
        Assert.assertTrue(subtileMap.containsKey("0.0:0.0"));
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase6() {
        Assert.assertTrue(subtileMap.containsKey(Float.toString(2000f / Constants.scale) + ":"
                + Float.toString(2000f / Constants.scale)));
    }

    @Test
    public void testFuntionGenerateSubtiles_testCase7() {
        Assert.assertEquals(
                "localhost:8080/sangraama",
                subtileMap.get(Float.toString(2000f / Constants.scale) + ":"
                        + Float.toString(2000f / Constants.scale)));
    }

    @Test
    public void testFuntionGetSubTileHost_testCase1() {
        Assert.assertEquals("localhost:8080/sangraama", TileCoordinator.INSTANCE.getSubTileHost(
                100f / Constants.scale, 100f / Constants.scale));
    }
    
    @Test
    public void testFuntionGetSubTileHost_testCase2() {
        Assert.assertEquals(null, TileCoordinator.INSTANCE.getSubTileHost(
                5000f / Constants.scale, 4000f / Constants.scale));
    }
}
