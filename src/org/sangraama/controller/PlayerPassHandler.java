package org.sangraama.controller;

import java.util.ArrayList;

import org.sangraama.asserts.Player;
import org.sangraama.asserts.SangraamaMap;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.coordination.ServerHandler;
import org.sangraama.coordination.ServerLocation;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.assets.TPlayer;
import org.sangraama.thrift.client.ThriftClient;

public enum PlayerPassHandler {
    INSTANCE;
    private static final String TAG = "PlayerPassHandler :";
    private ArrayList<Player> passPlayerList;
    private boolean isPass;
    private ServerHandler sHandler;
    private GameEngine gameEngine;

    private PlayerPassHandler() {
        passPlayerList = new ArrayList<Player>();
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
                 */
                // callThriftServer(player);

                passNewConnectionInfo(player);
                // this.gameEngine.addToRemovePlayerQueue(player);
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

    public void setPassPlayer(Player player) {
        this.passPlayerList.add(player);
        isPass = true;
        System.out.println(TAG + "Added passed player");
        this.run();
    }

    private void passNewConnectionInfo(Player player) {
        ServerLocation serverLoc = this.sHandler.getServerLocation(player.getX(), player.getY());

        if (serverLoc != null) {
            ClientTransferReq transferReq = new ClientTransferReq(player.getUserID(),
                    serverLoc.getServerURL(), serverLoc.getServerPort(), serverLoc.getDirectory());

            player.sendNewConnection(transferReq);
            System.out.println(TAG + " Sending new connection information. server URL:"
                    + serverLoc.getServerURL() + " serverPort:" + serverLoc.getServerPort());
        }
    }

}
