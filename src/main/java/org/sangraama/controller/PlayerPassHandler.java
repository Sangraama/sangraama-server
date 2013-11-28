package org.sangraama.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.assets.Ship;
import org.sangraama.coordination.ServerHandler;
import org.sangraama.coordination.ServerLocation;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.jsonprotocols.SendProtocol;
import org.sangraama.jsonprotocols.transfer.ClientTransferReq;
import org.sangraama.thrift.assets.TPlayer;
import org.sangraama.thrift.client.ThriftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PlayerPassHandler {
    INSTANCE;
    public static final Logger log = LoggerFactory.getLogger(PlayerPassHandler.class);
    private List<AbsPlayer> passPlayerList;
    private Map<Long, Player> passPlayerHash;
    private Map<String, Player> connectionHash;
    private volatile boolean isPass;
    private ServerHandler sHandler;

    private PlayerPassHandler() {
        this.passPlayerList = new ArrayList<>();
        this.passPlayerHash = new Hashtable<>();
        this.connectionHash = new Hashtable<>();
        this.sHandler = ServerHandler.INSTANCE;
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
//        log.info("Added passed player");
        this.run();
    }

    /**
     * Send parameters of new connection to get server updates
     * 
     * @param ship
     *            Player who need details about the server
     */
    public synchronized void setPassConnection(float x, float y, Player ship) {
        // this.connectionList.add(ship);
        this.connectionHash.put(Float.toString(x) + ":" + Float.toString(y), ship);
        this.isPass = true;
//        log.info("added to Pass Connection details");
        this.run();
    }

    private void passNewServerInfo(Player ship) {
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
        SendProtocol transferReq = new ClientTransferReq(30, ship.getUserID(), ship.getX(),
                ship.getY(), ship.getHealth(), ship.getScore(), newHost, ship.getType());
        /*log.info("new player pass server url " + newHost + " for x:" + ship.getX() + " y:"
                + ship.getY());*/
        ship.sendPassConnectionInfo(transferReq);
    }

    public void passPlayerToNewServer(Player player, String serverUrl){
        SendProtocol transferReq = new ClientTransferReq(30, player.getUserID(), player.getX(),
                player.getY(), player.getHealth(), player.getScore(), serverUrl, player.getType());

        player.sendPassConnectionInfo(transferReq);
    }

    /**
     * Send the connection details about new server that player needs to get updates
     * 
     * @param dummy
     *            Player that needs updates
     */
    private void passNewConnectionInfo(String key, Player dummy) {
        String[] s = key.split(":");
        String updateHost = (String) TileCoordinator.INSTANCE.getSubTileHost(
                Float.parseFloat(s[0]), Float.parseFloat(s[1]));
        /*
         * log.info("new update server url " + updateHost + " for x:" + dummy.getX() + " y:" +
         * dummy.getY());
         */
        SendProtocol transferReq = new ClientTransferReq(31, dummy.getUserID(), dummy.getX(),
                dummy.getY(), dummy.getHealth(), dummy.getScore(), updateHost, dummy.getType());
        dummy.sendUpdateConnectionInfo(transferReq);
    }

}