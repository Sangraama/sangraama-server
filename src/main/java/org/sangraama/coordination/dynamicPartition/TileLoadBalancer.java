package org.sangraama.coordination.dynamicPartition;

import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.common.Constants;
import org.sangraama.controller.PlayerPassHandler;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.gameLogic.queue.PlayerQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author Amila
 * This class responsible for dynamic load balancing
 * It keeps a count of number of players in the system and
 * then balance the load by passing exceeded players to another server
 */
public enum TileLoadBalancer implements Runnable {
    INSTANCE;
    private Logger log = LoggerFactory.getLogger(TileLoadBalancer.class);
    private TileCoordinator tileCoordinator;
    private SangraamaMap sangraamaMap;
    private GameEngine gameEngine;
    private PlayerQueue playerQueue;
    private PlayerPassHandler playerPassHandler;
    private int totalNoOfPlayersInServer;
    private Map<String, Integer> playerCountInSubTiles;
    private String serverUrl;

    TileLoadBalancer() {
        tileCoordinator = TileCoordinator.INSTANCE;
        sangraamaMap = SangraamaMap.INSTANCE;
        playerPassHandler = PlayerPassHandler.INSTANCE;
        gameEngine = GameEngine.INSTANCE;
        playerQueue = PlayerQueue.INSTANCE;
        playerCountInSubTiles = new HashMap<>();
        serverUrl = tileCoordinator.getServerURL();
    }

    @Override
    public void run() {

        Timer timer = new Timer(Constants.loadBalancingDelay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateServerPlayerCount();
                passPlayersToAnotherServer();

            }


        });
        timer.start();


    }

    private void updateServerPlayerCount() {
        countPlayersInServer();
    }

    /**
     * Count the total no of players in server by adding up players in sub tiles
     */
    private void countPlayersInServer() {
        List<String> subTileList = tileCoordinator.getSubtilesInServer();
        log.info("Sub tiles in server {}", subTileList);
        int totalCount = 0;
        for (String subTile : subTileList) {
            int subCount = countPlayersInSubTiles(subTile);
            totalCount += subCount;
        }
        log.info("Total no of players in server : {}", totalNoOfPlayersInServer);
        totalNoOfPlayersInServer = totalCount;
    }

    /**
     * Count no of players inside sub tile
     * @param subTile
     * @return no of players in sub tile
     */
    private int countPlayersInSubTiles(String subTile) {
        String[] s = subTile.split(":");
        if (s[0] != null && !s[0].equals(" ")) {
            float subTileOriginX = Float.valueOf(s[0]);
            float subTileOriginY = Float.valueOf(s[1]);
            float subTileMarginX = subTileOriginX + sangraamaMap.getSubTileWidth();
            float subTileMarginY = subTileOriginY + sangraamaMap.getSubTileHeight();

            int playerCount = 0;
            List<Player> playerList = gameEngine.getPlayerList();

            for (Player player : playerList) {
                if ((subTileOriginX < player.getX() && player.getX() < subTileMarginX) && (subTileOriginY < player.getY() && player.getY() < subTileMarginY)) {
                    playerCount++;
                }
            }
            playerCountInSubTiles.put(subTile, playerCount);
            return playerCount;
        }
        return 0;
    }

    /**
     * If no of players inside server exceed the limit
     * pass sub tile to another server
     */
    private void passPlayersToAnotherServer() {
        if (totalNoOfPlayersInServer > Constants.playersLimit) {
            log.info("Server exceeded the player limit...");
            String transferringTile = findSubTileToTransfer();
            if (transferringTile != null) {
                log.info("Found a sub tile to transfer : {}", transferringTile);
                List<Player> transferringPlayerList = findPlayersInsideTransferringTile(transferringTile);
                String newSvrUrl = findAServerToTransform(transferringPlayerList.size());
                if (newSvrUrl != null) {
                    for (Player player : transferringPlayerList) {
                        playerPassHandler.passPlayerToNewServer(player, newSvrUrl);
                        playerQueue.addToRemovePlayerQueue(player);
                    }
                    updateServerAfterTransfer(transferringTile, transferringPlayerList.size());
                    updateTransferredServerWithNewData(newSvrUrl, transferringPlayerList.size(), transferringTile);
                    log.info("Successfully transferred {}", transferringPlayerList.size() + " players to " + newSvrUrl);
                } else {
                    log.info("Could not find a server to transfer sub tile");
                }
            }
        } else {
            tileCoordinator.getPlayersCountByServerMap().put(serverUrl, totalNoOfPlayersInServer);
        }
    }

    /**
     * After sub tile transfer update server with new detail
     * @param svrUrl
     * @param transferredPlayers
     * @param transferringTile
     */
    private void updateTransferredServerWithNewData(String svrUrl, int transferredPlayers, String transferringTile) {
        int newCount = tileCoordinator.getPlayersCountByServerMap().get(svrUrl) + transferredPlayers;
        tileCoordinator.getPlayersCountByServerMap().put(svrUrl, newCount);
        tileCoordinator.getSubtileMap().put(transferringTile, svrUrl);
        List<String> str = tileCoordinator.getSubtilesInServerMap().get(svrUrl);
        str.add(transferringTile);
        tileCoordinator.getSubtilesInServerMap().put(svrUrl, str);
    }

    /**
     * After sub tile transfer update the server that received sub tile
     * @param transferringTile
     * @param transferringCount
     */
    private void updateServerAfterTransfer(String transferringTile, int transferringCount) {
        tileCoordinator.getPlayersCountByServerMap().put(serverUrl, totalNoOfPlayersInServer - transferringCount);
        List<String> str = tileCoordinator.getSubtilesInServerMap().get(serverUrl);
        str.remove(transferringTile);
        playerCountInSubTiles.remove(transferringTile);
        tileCoordinator.getSubtilesInServerMap().put(serverUrl, str);
        for (Player player : gameEngine.getPlayerList()) {
            player.sendTileSizeInfoAfterTilePass();
        }
    }

    /**
     * Search for a server that can keep extra players
     * @param transferringPlayers
     * @return
     */
    private String findAServerToTransform(int transferringPlayers) {
        Map<String, Integer> severs = tileCoordinator.getPlayersCountByServerMap();
        String serverUrl = null;
        for (String svr : severs.keySet()) {
            int count = severs.get(svr);
            int maxCount = 0;
            if ((count + transferringPlayers) < Constants.playersLimit) {
                if (count >= maxCount) {
                    maxCount = count;
                    serverUrl = svr;
                }
            }
        }
        return serverUrl;
    }

    /**
     * Find details of the players inside transferring tile
     * @param transferringTile
     * @return
     */
    private List<Player> findPlayersInsideTransferringTile(String transferringTile) {
        List<Player> transferringPlayerList = new ArrayList<>();
        String[] s = transferringTile.split(":");
        float subTileOriginX = Float.valueOf(s[0]);
        float subTileOriginY = Float.valueOf(s[1]);
        float subTileMarginX = subTileOriginX + sangraamaMap.getSubTileWidth();
        float subTileMarginY = subTileOriginY + sangraamaMap.getSubTileHeight();

        List<Player> playerList = gameEngine.getPlayerList();

        for (Player player : playerList) {
            if ((subTileOriginX < player.getX() && player.getX() < subTileMarginX) && (subTileOriginY < player.getY() && player.getY() < subTileMarginY)) {
                transferringPlayerList.add(player);
            }
        }
        return transferringPlayerList;
    }

    /**
     * Search for a sub tile to transfer
     * @return
     */
    private String findSubTileToTransfer() {
        int extraPlayerCount = totalNoOfPlayersInServer - Constants.playersLimit;
        int noOfPlayersToTransfer = 0;
        for (String key : playerCountInSubTiles.keySet()) {
            int subCount = playerCountInSubTiles.get(key);
            if (subCount > 0) {
                if (subCount > noOfPlayersToTransfer) {
                    noOfPlayersToTransfer = subCount;
                }
            }
        }

        for (String key : playerCountInSubTiles.keySet()) {
            int subCount = playerCountInSubTiles.get(key);
            if (subCount == noOfPlayersToTransfer) {
                return key;
            }
        }
        return null;
    }
}
