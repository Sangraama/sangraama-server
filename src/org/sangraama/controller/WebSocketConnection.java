package org.sangraama.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.sangraama.asserts.Player;
import org.sangraama.controller.clientprotocol.ClientEvent;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.gameLogic.PassedPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class WebSocketConnection extends MessageInbound {
    // Local Debug or logs
    private static boolean LL = true;
    private static boolean LD = true;
    private static final String TAG = "WebSocketConnection : ";
    public static final Logger log = LoggerFactory.getLogger(WebSocketConnection.class);

    private Player player;
    private Gson gson;

    public void setPlayer(Player player) {
        this.player = player;
        this.gson = new Gson();
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        // log.info("Open Connection");
        System.out.println(TAG + " Open Connection");
    }

    @Override
    protected void onClose(int status) {
        // log.info("Connection closed");
        System.out.println(TAG + " Close connection");
        if (this.player != null) {
            this.player.removeWebSocketConnection();
        }
    }

    @Override
    protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
        // log.warn("binary messages are not supported");
        System.out.println("Binary");
        throw new UnsupportedOperationException("not supported binary messages");
    }

    @Override
    protected void onTextMessage(CharBuffer charBuffer) throws IOException {
        this.gson = new Gson();
        String user = charBuffer.toString();
        ClientEvent clientEvent = gson.fromJson(user, ClientEvent.class);

        if (this.player != null) {
            switch (Integer.parseInt(clientEvent.getType())) {
                case 1: // setting user event request
                    this.player.setV(clientEvent.getV_x(), clientEvent.getV_y());
                    this.player.setAngle(clientEvent.getV_a());
                    this.player.shoot(clientEvent.getS());
                    System.out.println(TAG + " set user events " + clientEvent.getV_x() + " : "
                            + clientEvent.getV_y());
                    break;
                case 2: // requesting for interesting area
                    this.player.setInterestingIn(clientEvent.getX(), clientEvent.getY());
                    System.out.println(TAG + "player interesting in x:" + clientEvent.getX()
                            + " y:" + clientEvent.getY());
                    break;

                default:
                    break;
            }

        } else {
            if (clientEvent.getType().equals("1")) { // create new player & set the
                // connection
                this.player = new Player(clientEvent.getUserID(), clientEvent.getX(),
                        clientEvent.getY(), this);
                // PassedPlayer.INSTANCE.redirectPassPlayerConnection(p.getUserID(),
                // this);
                System.out.println(TAG + " Add new Player " + clientEvent.getUserID());
            }
        }
    }

    public void sendUpdate(List<PlayerDelta> deltaList) {
        this.gson = new Gson();

        try {
            String convertedString = gson.toJson(deltaList);
            getWsOutbound().writeTextMessage(CharBuffer.wrap(convertedString));

        } catch (IOException e) {
            System.out.println(TAG + " Unable to send update ");
            e.printStackTrace();
            log.error(TAG, e);
        }
    }

    /**
     * Send new connection details as a list. Because updates are send as a list, sending new single
     * connection details can't recognize by client side.
     * 
     * @param transferReq
     *            ArrayList<ClientTransferReq>
     */
    public void sendNewConnection(ArrayList<ClientTransferReq> transferReq) {
        this.gson = new Gson();
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(transferReq)));
        } catch (IOException e) {
            System.out.println(TAG + " Unable to send new connnection information");
            log.error(TAG, e);
        }
    }

    public void closeConnection() {
        try {
            getWsOutbound().flush();
            getWsOutbound().close(1, null);
        } catch (IOException e) {
            System.out.println(TAG + " Unable to close connnection ");
            log.error(TAG, e);
        }
    }
}