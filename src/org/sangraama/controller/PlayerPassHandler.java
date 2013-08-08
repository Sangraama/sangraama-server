package org.sangraama.controller;

import java.util.ArrayList;

import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.coordination.ServerHandler;
import org.sangraama.coordination.ServerLocation;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.assets.TPlayer;
import org.sangraama.thrift.client.ThriftClient;

public enum PlayerPassHandler {
    INSTANCE;
    private static final String TAG = "PlayerPassHandler :";
    private ArrayList<Player> passPlayerList;
    private ArrayList<Player> connectionList;
    private volatile boolean isPass;
    private ServerHandler sHandler;
    private GameEngine gameEngine;

    private PlayerPassHandler() {
        this.passPlayerList = new ArrayList<Player>();
        this.connectionList = new ArrayList<Player>();
        this.sHandler = ServerHandler.INSTANCE;
        this.gameEngine = GameEngine.INSTANCE;
    }

    public void run() {

        if (this.isPass) {
            // System.out.println(TAG + " is pass true");
            for (Player player : passPlayerList) {
                /*
                 * Pass player information into another server using Thrift. This technique removed
                 * by introducing a new concept of client is responsible for handling and connecting
                 * to other servers. Security issue of attacking by other players is avoid by
                 * assigning messages which are passed between players and decentralized servers.
                 * 
                 * @author : Gihan Karunarathne
                 */
                // callThriftServer(player);

                passNewServerInfo(player);
                // this.gameEngine.addToRemovePlayerQueue(player);
            }
            // Loop to send new deatils about update servers
            for (Player player : this.connectionList) {
                this.passNewConnectionInfo(player);
            }
            isPass = false;
            this.passPlayerList.clear();
        }

    }

    public void callThriftServer(Player player) {
        TPlayer tPlayer = new TPlayer();
        ServerLocation serverLoc = sHandler.getThriftServerLocation(player.getX(), player.getY());

        tPlayer.id = player.getUserID();
        tPlayer.x = (int) (player.getX() + SangraamaMap.INSTANCE.getOriginX());
        tPlayer.y = (int) (player.getY() + SangraamaMap.INSTANCE.getOriginY());
        tPlayer.v_x = player.getV().x;
        tPlayer.v_y = player.getV().y;
        if (serverLoc != null) {
            ThriftClient thriftClient = new ThriftClient(tPlayer, serverLoc.getServerURL(),
                    serverLoc.getServerPort());
            thriftClient.invoke();
        }
    }

    public synchronized void setPassPlayer(Player player) {
        this.passPlayerList.add(player);
        isPass = true;
        System.out.println(TAG + "Added passed player");
        this.run();
    }

    /**
     * Send parameters of new connection to get server updates
     * 
     * @param player
     *            Player who need details about the server
     */
    public synchronized void setPassConnection(Player player) {
        this.connectionList.add(player);
        this.isPass = true;
        System.out.println(TAG + " added to Pass Connection details");
        this.run();
    }

    private void passNewServerInfo(Player player) {
        /*
         * ServerLocation serverLoc = this.sHandler.getServerLocation(player.getX(), player.getY());
         * 
         * if (serverLoc != null) { ClientTransferReq transferReq = new
         * ClientTransferReq(player.getUserID(), serverLoc.getServerURL(),
         * serverLoc.getServerPort(), serverLoc.getDirectory());
         * 
         * player.sendNewConnection(transferReq); System.out.println(TAG +
         * " Sending new connection information. server URL:" + serverLoc.getServerURL() +
         * " serverPort:" + serverLoc.getServerPort()); }
         */

        String newHost = (String) TileCoordinator.INSTANCE.getSubTileHost(player.getX(),
                player.getY());
        ClientTransferReq transferReq = new ClientTransferReq(2, player.getUserID(), player.getX(),
                player.getY(), newHost);
        player.sendNewConnection(transferReq);
    }

    /**
     * Send the connection details about new server that player needs to get updates
     * @param player Player that needs updates
     */
    private void passNewConnectionInfo(Player player) {
        String updateHost = (String) TileCoordinator.INSTANCE.getSubTileHost(player.getX(),
                player.getY());
        ClientTransferReq transferReq = new ClientTransferReq(3, player.getUserID(), player.getX(),
                player.getY(), updateHost);
        player.sendNewConnection(transferReq);
    }

}
