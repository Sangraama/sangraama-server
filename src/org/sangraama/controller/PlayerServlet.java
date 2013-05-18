package org.sangraama.controller;

import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.sangraama.asserts.Player;
import org.sangraama.asserts.PlayerData;

@WebServlet("/org/sangraama/controller/playerservlet")
public class PlayerServlet extends WebSocketServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str,
	    HttpServletRequest req) {

	System.out.println("Trigger createWebSocketInBound");
	Random r = new Random();
	WebSocketConnection con = new WebSocketConnection();
	System.out.println("Player Data created");
	PlayerData p = new PlayerData(0, 0);
	System.out.println("Player creating");
	Player player = new Player(r.nextInt(100),con);
	System.out.println("Player created");
	con.setPlayer(player);
	return con;
    }

    // private static class WebSocketConnection extends MessageInbound {
    //
    // @Override
    // protected void onOpen(WsOutbound outbound) {
    // Constants.log.info("Open connection");
    // System.out.println("Open Connection");
    // }
    //
    // @Override
    // protected void onClose(int status) {
    // Constants.log.info("Connection closed");
    // System.out.println("Close connection");
    // }
    //
    // @Override
    // protected void onBinaryMessage(ByteBuffer byteBuffer)
    // throws IOException {
    // // log.warn("binary messages are not supported");
    // System.out.println("Binary");
    // throw new UnsupportedOperationException(
    // "not supported binary messages");
    // }
    //
    // @Override
    // protected void onTextMessage(CharBuffer charBuffer) throws IOException {
    // Gson gson = new Gson();
    // String user = charBuffer.toString();
    // // log.debug("Received message: {}", user);
    // System.out.println("REcieved msg :" + user);
    // Player player = gson.fromJson(user, Player.class);
    // System.out.println("x:" + player.getX() + " y:" + player.getY());
    // getWsOutbound().writeTextMessage(
    // CharBuffer.wrap(gson.toJson(player)));
    // }
    //
    // }

}
