package org.sangraama.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

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
    private Hashtable<Long, AbsPlayer> passPlayerHash;
    private ArrayList<AbsPlayer> connectionList;
    private Hashtable<String, AbsPlayer> connectionHash;
    private volatile boolean isPass;
    private ServerHandler sHandler;
    private GameEngine gameEngine;

    private PlayerPassHandler() {
        this.passPlayerList = new ArrayList<AbsPlayer>();
        this.passPlayerHash = new Hashtable<Long, AbsPlayer>();
        this.connectionList = new ArrayList<AbsPlayer>();
        this.connectionHash = new Hashtable<String, AbsPlayer>();
        this.sHandler = ServerHandler.INSTANCE;
        this.gameEngine = GameEngine.INSTANCE;
    }

    public synchronized void run() {

        if (this.isPass) {
            // System.out.println(TAG + " is pass true");
            if (!this.passPlayerHash.isEmpty()) {
                Set<Long> s = this.passPlayerHash.keySet();
                for (long key : s) {
                    /*
                     * Pass player information into another server using Thrift. This technique
                     * removed by introducing a new concept of client is responsible for handling
                     * and connecting to other servers. Security issue of attacking by other players
                     * is avoid by assigning messages which are passed between players and
                     * decentralized servers.
                     */
                    // callThriftServer(player);

                    this.passNewServerInfo(this.passPlayerHash.remove(key));

                    // this.passNewServerInfo(ship);
                    // this.passPlayerList.remove(ship);
                    // this.gameEngine.addToRemovePlayerQueue(player);
                }
            }
            // Loop to send new deatils about update servers
            if (!this.connectionHash.isEmpty()) {
                Set<String> s = this.connectionHash.keySet();
                for (String key : s) {
                    // this.passNewConnectionInfo(ship);
                    // this.connectionList.remove(ship);
                    this.passNewConnectionInfo(key, this.connectionHash.remove(key));
                }
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

    /**
     * Send parameters of the new server which player is going to locate
     * 
     * @param ship
     *            Player who need to transfer into new server
     */
    public synchronized void setPassPlayer(Player ship) {
        // this.passPlayerList.add(ship);
        this.passPlayerHash.put(ship.getUserID(), ship);
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
    public synchronized void setPassConnection(float x, float y, AbsPlayer ship) {
        // this.connectionList.add(ship);
        this.connectionHash.put(Float.toString(x) + ":" + Float.toString(y), ship);
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
        
        String newHost = (String) TileCoordinator.INSTANCE.getSubTileHost(ship.getX(), ship.getY());
        ClientTransferReq transferReq = new ClientTransferReq(2, ship.getUserID(), ship.getX(),
                ship.getY(), ship.getHealth(), ship.getScore(), newHost);
        ship.sendNewConnection(transferReq);
    }

    /**
     * Send the connection details about new server that player needs to get updates
     * 
     * @param ship
     *            Player that needs updates
     */
    private void passNewConnectionInfo(String key, AbsPlayer ship) {
        String[] s = key.split(":");
        String updateHost = (String) TileCoordinator.INSTANCE.getSubTileHost(
                Float.parseFloat(s[0]), Float.parseFloat(s[1]));
        System.out.println(TAG + " new update server url " + updateHost + " for x:" + ship.getX()
                + " y:" + ship.getY());
        ClientTransferReq transferReq = new ClientTransferReq(3, ship.getUserID(), ship.getX(),
                ship.getY(), ship.getHealth(), ship.getScore(), updateHost);
        ship.sendConnectionInfo(transferReq);
    }

}