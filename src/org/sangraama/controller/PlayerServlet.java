package org.sangraama.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.sangraama.asserts.Player;

@WebServlet("/org/sangraama/controller/playerservlet")
public class PlayerServlet extends WebSocketServlet {

    private int id = 0;
    private int test = 0;
    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str,
	    HttpServletRequest req) {
	
	
	System.out.println("string " + str);
	switch (str) {
	case "NewPlayer":
	    System.out.println("Trigger newplayer");
	    Random r = new Random();
	    WebSocketConnection con = new WebSocketConnection();

	    System.out.println("New Player creating");
	    Player player = new Player(r.nextInt(100), con);
	    System.out.println("Player created");
	    con.setPlayer(player);
	    return con;

	case "PassPlayer":
	    System.out.println("Trigger passplayer");
	    WebSocketConnection ncon = new WebSocketConnection();

	    System.out.println("Pass Player with test:" + this.test);
	    Player nplayer = new Player(this.id, ncon);
	    System.out.println("Player created");
	    ncon.setPlayer(nplayer);
	    return ncon;
	default:
	    return null;
	}

    }

    @Override
    protected String selectSubProtocol(List<String> subProtocols) {
	Random r = new Random();
	int tmp = r.nextInt(100);
	this.test += tmp;
	System.out.println("to test :" + this.test + " added:" + tmp);

	for (String str : subProtocols) {
	    System.out.println("Sub pro :" + str);
	}
	if (subProtocols.get(2) == "10") {
	    this.id = Integer.parseInt(subProtocols.get(2));

	    return subProtocols.get(0);
	} else {
	    System.out.println("newplayer subp");
	    return subProtocols.get(0);
	}
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
