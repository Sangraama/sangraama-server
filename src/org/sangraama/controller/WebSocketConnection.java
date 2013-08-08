package org.sangraama.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.controller.clientprotocol.ClientEvent;
import org.sangraama.controller.clientprotocol.ClientTransferReq;
import org.sangraama.controller.clientprotocol.PlayerDelta;
import org.sangraama.controller.clientprotocol.TileInfo;
import org.sangraama.controller.clientprotocol.TransferInfo;
import org.sangraama.util.VerifyMsg;
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

    public WebSocketConnection() {
        this.gson = new Gson();
    }

    /**
     * Set the player who is own this web socket connection
     * 
     * @param ship
     *            the instance of player which is connect to client
     */
    public void setPlayer(Ship player) {
        this.player = player;
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
                    this.player.reqInterestIn(clientEvent.getX(), clientEvent.getY());
                    System.out.println(TAG + "player interesting in x:" + clientEvent.getX()
                            + " & y:" + clientEvent.getY());
                    break;

                default:
                    break;
            }

        } else {
            if (clientEvent.getType().equals("1")) { // create new player & set the
                // connection
                this.player = new Ship(clientEvent.getUserID(), clientEvent.getX(),
                        clientEvent.getY(), this);
                System.out.println(TAG + " Add new Player " + clientEvent.getUserID());
                this.player.setV(clientEvent.getV_x(), clientEvent.getV_y());
                this.player.setAngle(clientEvent.getV_a());
                //this.player.shoot(clientEvent.getS());
                System.out.println(TAG + " set user events " + clientEvent.getV_x() + " : "
                        + clientEvent.getV_y() + " when creating player");
                
            }else if (clientEvent.getType().equals("2")) {
                TransferInfo playerInfo;
                String info = clientEvent.getInfo();
                byte[] signedInfo = clientEvent.getSignedInfo();
                boolean msgVerification = VerifyMsg.INSTANCE.verifyMessage(info, signedInfo);
                if(msgVerification){
                    playerInfo = gson.fromJson(info, TransferInfo.class);
                    this.player = new Ship(clientEvent.getUserID(), playerInfo.getPositionX(),
                            playerInfo.getPositionY(), this);
                    System.out.println(TAG + "Adding player from another server to GameEngine.");
                }
            }
        }
    }

    /**
     * Send new updates of players states to the particular client
     * 
     * @param playerDeltaList
     *            delta updates of players who are located inside AOI
     */
    public void sendUpdate(List<PlayerDelta> playerDeltaList) {
        try {
            String convertedString = gson.toJson(playerDeltaList);
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
     *            details about new connection server ArrayList<ClientTransferReq>
     */
    public void sendNewConnection(ArrayList<ClientTransferReq> transferReq) {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(transferReq)));
            System.out.println(TAG + " new con details " + gson.toJson(transferReq));
        } catch (IOException e) {
            System.out.println(TAG + " Unable to send new connnection information");
            log.error(TAG, e);
        }
    }

    /**
     * Send coordination details about tile size on this server
     * 
     * @param tilesInfo
     *            ArrayList of details about tile of current server
     */
    public void sendTileSizeInfo(ArrayList<TileInfo> tilesInfo) {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(tilesInfo)));
            System.out.println(TAG + " send size of tile " + gson.toJson(tilesInfo));
        } catch (IOException e) {
            System.out.println(TAG + " Unable to send tile size information");
            log.error(TAG, e);
        }
    }

    /**
     * Send coordination detail about tile
     * 
     * @param tileInfo
     *            details about tile
     */
    public void sendTileSizeInfo(TileInfo tileInfo) {
        ArrayList<TileInfo> tilesInfo = new ArrayList<>();
        tilesInfo.add(tileInfo);
        this.sendTileSizeInfo(tilesInfo);
    }

    /**
     * Close the WebSocket connection of the player
     * 
     * @return null
     */
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