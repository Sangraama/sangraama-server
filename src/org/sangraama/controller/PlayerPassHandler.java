package org.sangraama.controller;

import java.util.ArrayList;

import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
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
    private ArrayList<AbsPlayer> passPlayerList;
    private ArrayList<AbsPlayer> connectionList;
    private volatile boolean isPass;
    private ServerHandler sHandler;
    private GameEngine gameEngine;

    private PlayerPassHandler() {
        this.passPlayerList = new ArrayList<AbsPlayer>();
        this.connectionList = new ArrayList<AbsPlayer>();
        this.sHandler = ServerHandler.INSTANCE;
        this.gameEngine = GameEngine.INSTANCE;
    }

    public void run() {

        if (this.isPass) {
            // System.out.println(TAG + " is pass true");
            for (AbsPlayer ship : passPlayerList) {
                /*
                 * Pass player information into another server using Thrift. This technique removed
                 * by introducing a new concept of client is responsible for handling and connecting
                 * to other servers. Security issue of attacking by other players is avoid by
                 * assigning messages which are passed between players and decentralized servers.
                 * 
                 *
                 */
                // callThriftServer(player);

                passNewServerInfo(ship);
                // this.gameEngine.addToRemovePlayerQueue(player);
            }
            // Loop to send new deatils about update servers
            for (AbsPlayer ship : this.connectionList) {
                this.passNewConnectionInfo(ship);
            }
            isPass = false;
            this.passPlayerList.clear();
        }

    }

    public void callThriftServer(Ship ship) {
        TPlayer tPlayer = new TPlayer();
        ServerLocation serverLoc = sHandler.getThriftServerLocation(ship.getX(), ship.getY());

        tPlayer.id = ship.getUserID();
        tPlayer.x = (int) (ship.getX() + SangraamaMap.INSTANCE.getOriginX());
        tPlayer.y = (int) (ship.getY() + SangraamaMap.INSTANCE.getOriginY());
        tPlayer.v_x = ship.getV().x;
        tPlayer.v_y = ship.getV().y;
        if (serverLoc != null) {
            ThriftClient thriftClient = new ThriftClient(tPlayer, serverLoc.getServerURL(),
                    serverLoc.getServerPort());
            thriftClient.invoke();
        }
    }

    public synchronized void setPassPlayer(Player ship) {
        this.passPlayerList.add(ship);
        isPass = true;
        System.out.println(TAG + "Added passed player");
        this.run();
    }

    /**
     * Send parameters of new connection to get server updates
     * 
     * @param ship
     *            Player who need details about the server
     */
    public synchronized void setPassConnection(AbsPlayer ship) {
        this.connectionList.add(ship);
        this.isPass = true;
        System.out.println(TAG + " added to Pass Connection details");
        this.run();
    }

    private void passNewServerInfo(AbsPlayer ship) {
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

        String newHost = (String) TileCoordinator.INSTANCE.getSubTileHost(ship.getX(),
                ship.getY());
        ClientTransferReq transferReq = new ClientTransferReq(2, ship.getUserID(), ship.getX(),
                ship.getY(), newHost);
        ship.sendNewConnection(transferReq);
    }

    /**
     * Send the connection details about new server that player needs to get updates
     * @param ship Player that needs updates
     */
    private void passNewConnectionInfo(AbsPlayer ship) {
        String updateHost = (String) TileCoordinator.INSTANCE.getSubTileHost(ship.getX(),
                ship.getY());
        ClientTransferReq transferReq = new ClientTransferReq(3, ship.getUserID(), ship.getX(),
                ship.getY(), updateHost);
        ship.sendNewConnection(transferReq);
    }

}
