package org.sangraama.coordination.dynamciPartition;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.assets.Ship;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.coordination.dynamicPartition.TileLoadBalancer;
import org.sangraama.gameLogic.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestTileLoadBalancer {
    private HazelcastInstance hazelcastInstance;
    private Map<String, String> subtileMap;
    private Map<String, Integer> playersCountByServerMap;
    private Map<String, List<String>> subtilesByUrlMap;
    private SangraamaMap sangraamaMap;
    private Properties prop;
    private String serverURL;
    private int serverPort;
    private GameEngine gameEngine;
    private TileLoadBalancer tileLoadBalancer;


    @Before
    public void setUp() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(new Config());
        tileLoadBalancer = TileLoadBalancer.INSTANCE;
        this.subtileMap = hazelcastInstance.getMap("subtile");
        this.playersCountByServerMap = hazelcastInstance.getMap("playersCountByServerMap");
        this.subtilesByUrlMap = hazelcastInstance.getMap("subtilesByUrlMap");
        sangraamaMap = SangraamaMap.INSTANCE;
        gameEngine = GameEngine.INSTANCE;
        serverPort = 8080;
        serverURL = "localhost:8080/sangraama/sangraama/player";
        readPropertyDetails();
        addPlayers();
        generateMaps();
    }

    private void addPlayers() {
        Ship s1 = new Ship(1, 50, 50, 1000, 500, 100, 0, new WebSocketConnection(), 1, 1);
        Ship s2 = new Ship(1, 73, 64, 1000, 500, 100, 0, new WebSocketConnection(), 1, 1);
        Ship s3 = new Ship(1, 86, 79, 1000, 500, 100, 0, new WebSocketConnection(), 1, 1);
        gameEngine.getPlayerList().add(s1);
        gameEngine.getPlayerList().add(s2);
        gameEngine.getPlayerList().add(s3);
    }

    private void readPropertyDetails() {
        this.prop = new Properties();
        try {
            this.prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
        } catch (Exception e) {
        }
        sangraamaMap.setMap(prop.getProperty("maporiginx"),
                prop.getProperty("maporiginy"),
                Float.parseFloat(prop.getProperty("mapwidth")),
                Float.parseFloat(prop.getProperty("mapheight")), prop.getProperty("host") + ":"
                + prop.getProperty("port") + "/" + prop.getProperty("dir")
                + "/sangraama/player", Float.parseFloat(prop.getProperty("maxlength")),
                Float.parseFloat(prop.getProperty("maxheight")));

        sangraamaMap.setSubTileProperties(
                Float.parseFloat(prop.getProperty("subtilewidth")),
                Float.parseFloat(prop.getProperty("subtileheight")));
    }

    private void generateMaps() {
        String subTileOrigins;
        List<String> subTileOriginList = new ArrayList<>();
        float subTileOriginX = 0, subTileOriginY = 0;
        Float originX = sangraamaMap.getOriginX();
        Float originY = sangraamaMap.getOriginY();
        for (int i = 0; i < sangraamaMap.getMapWidth() / sangraamaMap.getSubTileWidth(); i++) {
            for (int j = 0; j < sangraamaMap.getMapHeight() / sangraamaMap.getSubTileHeight(); j++) {
                if (originX != null) {
                    subTileOriginX = (i * sangraamaMap.getSubTileWidth()) + originX;
                    subTileOriginY = (j * sangraamaMap.getSubTileHeight()) + originY;
                    subTileOrigins = Float.toString(subTileOriginX) + ":"
                            + Float.toString(subTileOriginY);
                } else {
                    subTileOrigins = " : ";

                }
                subtileMap.put(subTileOrigins, serverURL);
                subTileOriginList.add(subTileOrigins);
            }
        }
        playersCountByServerMap.put(serverURL, 0);
        subtilesByUrlMap.put(serverURL, subTileOriginList);
    }

    @Test
    public void testCountPlayersInSubTileTestCase1() {
        int count = tileLoadBalancer.countPlayersInSubTiles("0:0");
        Assert.assertTrue(count == 1);
    }

    @Test
    public void testCountPlayersInSubTileTestCase2() {
        int count = tileLoadBalancer.countPlayersInSubTiles("62.5:62.5");
        Assert.assertTrue(count == 4);
    }
    @Test
    public void testCountPlayersInServer1(){
        int totCount = tileLoadBalancer.countPlayersInServer();
        Assert.assertTrue(totCount == 9);
    }

    @Test
    public void testFindSubTileToTransfer(){
        String subTile = tileLoadBalancer.findSubTileToTransfer();
        Assert.assertTrue(subTile.equals("62.5:62.5"));
    }
    
    @Test
    public void testFindPlayersInsideTransferringTile(){
        List<Player> playerList = tileLoadBalancer.findPlayersInsideTransferringTile("62.5:62.5");
        Assert.assertTrue(playerList.size() == 10);
    }
}
