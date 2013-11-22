package org.sangraama.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.gameLogic.queue.DummyQueue;
import org.sangraama.jsonprotocols.SendProtocol;
import org.sangraama.jsonprotocols.receive.ClientEvent;
import org.sangraama.jsonprotocols.transfer.BulletTransferReq;
import org.sangraama.jsonprotocols.transfer.ScoreChangeTransferReq;
import org.sangraama.util.VerifyMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class WebSocketConnection extends MessageInbound {
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
        this.player = null;
        this.player = player;
        //log.info("created a PLAYER conection...");
    }

    public void setDummyPlayer(DummyPlayer dummyPlayer) {
        this.player = null;
        this.player = dummyPlayer;
//        log.info("created a DUMMY PLAYER conection...");
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        // log.info("Open Connection");
    }

    @Override
    protected void onClose(int status) {
        // log.info("Connection closed");
        this.player.removeWebSocketConnection();
        this.player = null;

        log.info("Close connection");
    }

    @Override
    protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
        // log.warn("binary messages are not supported");
        System.out.println("Binary");
        throw new UnsupportedOperationException("not supported binary messages");
    }

    @Override
    protected void onTextMessage(CharBuffer charBuffer) throws IOException {
        String clientMsg = charBuffer.toString();
        ClientEvent event = gson.fromJson(clientMsg, ClientEvent.class);

        // Avoid checking whether player is created every time access it.
        try {
            this.playerEvents(event);
        } catch (Exception e) {
            log.error("Error occured while processing message {}", e);
        }
    }

    private void playerEvents(ClientEvent event) {
        String T = " playerevent ";
        switch (event.getType()) {
            case 1: // setting user event request
                this.player.setV(event.getV_x(), event.getV_y());
                this.player.setAngle(event.getA());
                this.player.setAngularVelocity(event.getDa());
                this.player.shoot(event.getS());
                // System.out.println(TAG + T + " set user events " + event.getV_x() + " : "
                // + event.getV_y());
                break;

            case 2: // requesting for interesting area
                this.player.reqInterestIn(event.getX(), event.getY());
                // System.out.println(TAG + T + "player interesting in x:" + event.getX() + " & y:"
                // + event.getY());
                break;

            case 3: // set AOI of the player
                this.player.setAOI(event.getW(), event.getH());
                // System.out.println(TAG + T + " set AOI of player: " + event.getUserID());
                break;

            case 4: // Reset settings and make dummy player
                this.player.setV(0, 0);
                this.player.setAngle(0);
                this.player.shoot(0);
//                log.info(T + " RESET user events ");
                break;

            case 5: // Set Virtual point as the center of AOI in order to get updates
                this.player.setVirtualPoint(event.getX_vp(), event.getY_vp());
                break;

            case 20: /*
                      * Adding the bullet to the game world of the server. The bullet was passed
                      * from the neighbor server. First the information is verified to check whether
                      * it is changed by the client or not. Then dummy player add bullets to the
                      * world.
                      */
                if (VerifyMsg.INSTANCE.verifyMessage(event.getInfo(), event.getSignedInfo())) {
                    BulletTransferReq bulletTransReq = gson.fromJson(event.getInfo(),
                            BulletTransferReq.class);
                    Bullet bullet = bulletTransReq.reCreateBullet(event.getInfo());
                    ((DummyPlayer) this.player).addBulletToGameWorld(bullet);
                }

                break;

            case 21: // Get the score change message from another server
                if (VerifyMsg.INSTANCE.verifyMessage(event.getInfo(), event.getSignedInfo())) {
                    ScoreChangeTransferReq scoreChangeReq = gson.fromJson(event.getInfo(),
                            ScoreChangeTransferReq.class);
                    float scoreChange = scoreChangeReq.getScore(event.getInfo());
                    ((Player) this.player).setScore(scoreChange);
                }
                break;

            case 30: /*
                      * Create a player
                      * 
                      * @case 1: if already the player => ignore
                      * 
                      * @case 2: if it's a dummy player => change it to player <include
                      * authentication>
                      */
                if (this.player != null && !this.player.isPlayer()) {
                    /*
                     * Remove already existing dummy player (null values aren't allowed by
                     * ConcurrentLinkedQueue)
                     */
                    DummyQueue.INSTANCE.addToRemoveDummyQueue((DummyPlayer) this.player);
                }

                this.setPlayer(new Ship(event.getUserID(), event.getX(), event.getY(),
                        event.getW(), event.getH(), 100, 0, this, event.getSt(), event.getBt()));
                this.player.setV(event.getV_x(), event.getV_y());
                this.player.setAngle(event.getA());
                this.player.setVirtualPoint(event.getX_vp(), event.getY_vp());

//                log.info(T + " add new Player " + event.toString());
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
                this.setDummyPlayer(new DummyPlayer(event.getUserID(), event.getW(), event.getH(),
                        this));
                this.player.setVirtualPoint(event.getX_vp(), event.getY_vp());
//                log.info(T + " add new dummy player: " + event.toString());
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
    public void sendUpdate(List<SendProtocol> playerDeltaList) throws IOException {
        String convertedString = gson.toJson(playerDeltaList);
        getWsOutbound().writeTextMessage(CharBuffer.wrap(convertedString));
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
            log.error("Unable to send new connnection information {}", e);
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
            log.error("Unable to send tile size information {}", e);
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

    /**
     * Send the information of the transferring object to the client.
     * 
     * @param tranferReqList
     */
    public void sendPassGameObjInfo(List<SendProtocol> tranferReqList) {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(tranferReqList)));
            log.info("send bullet transfer message : " + gson.toJson(tranferReqList));
        } catch (IOException e) {
            log.error("Unable to send passing game objects information {}", e);
        }
    }

    /**
     * Send the score change when a player in another server shoots a player in another server
     * 
     * @param scoreChangeReq
     */
    public void sendScoreChangeReq(List<SendProtocol> scoreChangeReq) {
        try {
            getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(scoreChangeReq)));
            log.info("send score change to player : " + gson.toJson(scoreChangeReq));
        } catch (IOException e) {
            log.error("Unable to send score change information {}", e);
        }
    }

    /**
     * Send coordination detail abo }
     * 
     * /** Close the WebSocket connection of the player
     * 
     * @return null
     */
    public void closeConnection() {
        try {
            getWsOutbound().flush();
            getWsOutbound().close(1, null);
        } catch (IOException e) {
            log.error("Unable to close connnection ");
        }
    }
}
