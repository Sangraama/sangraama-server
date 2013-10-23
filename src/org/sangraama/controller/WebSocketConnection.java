package org.sangraama.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.controller.clientprotocol.ClientEvent;
import org.sangraama.controller.clientprotocol.DefeatMsg;
import org.sangraama.controller.clientprotocol.SendProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class WebSocketConnection extends MessageInbound {
    // Local Debug or logs
    private static boolean LL = true;
    private static boolean LD = true;
    private static final String TAG = "WebSocketConnection : ";
    public static final Logger log = LoggerFactory.getLogger(WebSocketConnection.class);

    private AbsPlayer player = null;
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

        if (this.player != null) { // this.player.removeWebSocketConnection(); // reuse already
                                   // established connection
            this.player = null;
        }

        this.player = player;
        System.out.println(TAG + " created a PLAYER conection...");
    }

    public void setDummyPlayer(DummyPlayer dummyPlayer) {

        if (this.player != null) { // this.player.removeWebSocketConnection(); // reuse already
                                   // established connection
            this.player = null;
        }

        this.player = dummyPlayer;
        System.out.println(TAG + " created a DUMMY PLAYER conection...");
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        // log.info("Open Connection");
        System.out.println(TAG + " Open Connection");
    }

    @Override
    protected void onClose(int status) {
        // log.info("Connection closed");

        if (this.player != null) {
            this.player.removeWebSocketConnection();
            this.player = null;
        }

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
        String user = charBuffer.toString();
        ClientEvent event = gson.fromJson(user, ClientEvent.class);

        // Avoid checking whether player is created every time access it.
        try {
            System.out.println(TAG + " player event call " + event.getType());
            this.playerEvents(event);
        } catch (Exception e) {
            System.out.println(TAG + " create new player call " + event.getType());
            // this.createNewPlayer(event);
            // System.out.print(TAG + "Player not found ");
            // e.printStackTrace();
        }

        // if (this.player != null) {
        // /* Call when "the player" is already connected and added to the server world map */
        // this.playerEvents(event);
        // } else if (this.dPlayer != null) {
        // /* Call when "a Dummy Player" is already created and sending updates to the client */
        // this.dummyPlayerEvents(event);
        // } else {
        // /* Call when a player is not created */
        // this.newPlayerEvent(event);
        // }
    }

    private void playerEvents(ClientEvent event) {
        String T = " playerevent ";
        switch (event.getType()) {
            case 1: // setting user event request
                this.player.setV(event.getV_x(), event.getV_y());
                this.player.setAngle(event.getA());
                this.player.setAngularVelocity(event.getDa());
                this.player.shoot(event.getS());
                System.out.println(TAG + T + " set user events " + event.getV_x() + " : "
                        + event.getV_y());
                break;
            case 2: // requesting for interesting area
                this.player.reqInterestIn(event.getX(), event.getY());
                System.out.println(TAG + T + "player interesting in x:" + event.getX() + " & y:"
                        + event.getY());
                break;
            // set AOI of the player
            case 3:
                this.player.setAOI(event.getW(), event.getH());
                System.out.println(TAG + T + " set AOI of player: " + event.getUserID());
                break;
            case 4: // Reset settings and make dummy player
                this.player.setV(event.getV_x(), event.getV_y());
                this.player.setAngle(event.getA());
                this.player.shoot(event.getS());
                System.out.println(TAG + T + " RESET user events " + event.getV_x() + " : "
                        + event.getV_y());
                break;
            case 5: // Set Virtual point as the center of AOI in order to get updates
                this.player.setVirtualPoint(event.getX_vp(), event.getY_vp());
                break;

            case 30: /*
                      * Create a player
                      * 
                      * @case 1: if already the player => ignore
                      * 
                      * @case 2: if it's a dummy player => change it to player <include
                      * authentication>
                      */
                this.setPlayer(new Ship(event.getUserID(), event.getX(), event.getY(),
                        event.getW(), event.getH(), 100, 0, this));
                this.player.setV(event.getV_x(), event.getV_y());
                this.player.setAngle(event.getA());
                this.player.setVirtualPoint(event.getX_vp(), event.getY_vp());
                System.out.println(TAG + T + " add new Player " + event.toString());
                /*
                 * AOI and Virtual point will add to the player after creation of it NOTE: These
                 * player details should retrieved via a encrypted message. To create player type:
                 * login server or current client's primary (when passing the player) will provide
                 * the encrypted message To create dummy player: in order to create a dummy player,
                 * client will ask for AOI point. Then current primary server will send a encrypted
                 * message which is can use to create a dummy player
                 */
                break;
            case 31: /*
                      * Create a dummy player
                      * 
                      * @case 1: if already a dummy player => ignore
                      * 
                      * @case 2: if it's a player => change it to dummy player <include
                      * authentication>
                      */
                this.setDummyPlayer(new DummyPlayer(event.getUserID(), event.getX(), event.getY(),
                        event.getW(), event.getH(), this));
                this.player.setVirtualPoint(event.getX_vp(), event.getY_vp());
                System.out.println(TAG + T + " add new dummy player: " + event.toString());
                break;
            default:
                break;
        }
    }

    /*******************************************************************************
     * Pushing details into the web socket
     *******************************************************************************/

    /**
     * Send new updates of players states to the particular client
     * 
     * @param playerDeltaList
     *            delta updates of players who are located inside AOI
     */
    public void sendUpdate(List<SendProtocol> playerDeltaList) {
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
    public void sendNewConnection(ArrayList<SendProtocol> transferReq) {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(transferReq)));
            // System.out.println(TAG + " new con details " + gson.toJson(transferReq));
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
    public void sendTileSizeInfo(List<SendProtocol> tilesInfo) {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(tilesInfo)));
            // System.out.println(TAG + " send size of tile " + gson.toJson(tilesInfo));
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
    public void sendTileSizeInfo(SendProtocol tileInfo) {
        List<SendProtocol> tilesInfo = new ArrayList<SendProtocol>();
        tilesInfo.add(tileInfo);
        this.sendTileSizeInfo(tilesInfo);
    }

    public void sendPlayerDefeatMsg(Player player) {
        try {
            DefeatMsg defeatMsg = new DefeatMsg(6, player.getUserID(), 100, 100, player.getScore());
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(defeatMsg)));
        } catch (IOException e) {
            System.out.println(TAG + " Unable to send player defeat message.");
            log.error(TAG, e);
        }
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
