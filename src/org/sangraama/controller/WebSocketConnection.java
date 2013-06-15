package org.sangraama.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

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

    private Player player = null;

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        //log.info("Open Connection");
        System.out.println(TAG + " Open Connection");
    }

    @Override
    protected void onClose(int status) {
        //log.info("Connection closed");
        System.out.println(TAG + " Close connection");
    }

    @Override
    protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
        // log.warn("binary messages are not supported");
        System.out.println("Binary");
        throw new UnsupportedOperationException("not supported binary messages");
    }

    @Override
    protected void onTextMessage(CharBuffer charBuffer) throws IOException {
        Gson gson = new Gson();
        String user = charBuffer.toString();
        // Constants.log.debug("Received message: {}", user);
        System.out.println(TAG+" REcieved msg :" + user);

        ClientEvent p = gson.fromJson(user, ClientEvent.class);
        if (this.player != null) {
            this.player.setV(p.getV_x(), p.getV_y());
            System.out.println(TAG + " set user events "+p.getV_x()+" : "+p.getV_y());
        } else {
            if ( "setcon".equalsIgnoreCase(p.getType())) { // set the connection
               PassedPlayer.INSTANCE.redirectPassPlayerConnection(p.getUserID(), this);
               System.out.println(TAG + " Add to set connection");
            }
        }
    }

    public void sendUpdate(ArrayList<PlayerDelta> deltaList) {
        Gson gson = new Gson();

            try {
                getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(deltaList)));
            } catch (IOException e) {
                System.out.println(TAG + " Unable to send update");
                log.error(TAG, e);
            }
    }

    public void sendNewConnection(ClientTransferReq transferReq) {
        Gson gson = new Gson();
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(transferReq)));
            getWsOutbound().flush();
            getWsOutbound().close(1, null);
        } catch (IOException e) {
            System.out.println(TAG + " Unable to send new connnection information");
            log.error(TAG, e);
        }
    }
}