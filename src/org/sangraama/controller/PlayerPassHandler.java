package org.sangraama.controller;

import java.util.ArrayList;

import org.sangraama.asserts.Player;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.coordination.ServerHandler;
import org.sangraama.coordination.ServerLocation;
import org.sangraama.thrift.assets.TPlayer;
import org.sangraama.thrift.client.ThriftClient;

public enum PlayerPassHandler implements Runnable {
    INSTANCE;
    private static final String TAG = "WebSocketConnection";
    private ArrayList<Player> passPlayerList = null;
    private boolean isPass = false;
    private ServerHandler sHandler = null;

    private PlayerPassHandler() {
        passPlayerList = new ArrayList<Player>();
        this.sHandler = ServerHandler.INSTANCE;
    }

    public void run() {
        while (true) {
            if (isPass) {
                if (!passPlayerList.isEmpty()) {
                    passPlayerList.get(0);
                    callThriftServer(passPlayerList.get(0));
                    passNewConnectionInfo(passPlayerList.get(0));
                    passPlayerList.remove(0);
                } else {
                    isPass = false;
                }
            }
        }
    }

    public void callThriftServer(Player player) {
        TPlayer tPlayer = new TPlayer();
        ServerLocation serverLoc = sHandler.getThriftServerLocation(player.getX(), player.getY());

        tPlayer.id = player.getUserID();
        tPlayer.x = (int) player.getX();
        tPlayer.y = (int) player.getY();
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
    }

    private void passNewConnectionInfo(Player player) {
        ServerLocation serverLoc = this.sHandler.getServerLocation(player.getX(),
                player.getY());
        ClientTransferReq transferReq = new ClientTransferReq(player.getUserID(),
                serverLoc.getServerURL(), serverLoc.getServerPort());
        player.sendNewConnection(transferReq);
        System.out.println(TAG + " Sending new connection information. server URL:"
                + serverLoc.getServerURL() + " serverPort:" + serverLoc.getServerPort());
    }

}
