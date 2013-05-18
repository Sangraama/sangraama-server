package org.sangraama.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.sangraama.asserts.Player;
import org.sangraama.asserts.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Servlet implementation class EventHandle
 */
@WebServlet("/org/sangraama/controller/EventHandler")
public class EventHandler extends WebSocketServlet {

  /**
     * 
     */
    private static final long serialVersionUID = 1L;



/**
   * @see WebSocketServlet#WebSocketServlet()
   */
  public EventHandler() {
    super();
  }

  // private static LoggerFactory.getLogger Logger log = (EventHandler. class );
  private static Logger log = LoggerFactory.getLogger(EventHandler.class);

  

  @Override
  protected StreamInbound createWebSocketInbound(String subprotocol,
      HttpServletRequest request) {
    System.out.println("Trigger createWebSocketInBound");
    
    return new WebSocketConnection();
  }

  private static class WebSocketConnection extends MessageInbound {

    @Override
    protected void onOpen(WsOutbound outbound) {
      log.info("Open connection");
      System.out.println("Open Connection");
    }

    @Override
    protected void onClose(int status) {
      //log.info("Connection closed");
      System.out.println("Close connection");
    }

    @Override
    protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
      //log.warn("binary messages are not supported");
      System.out.println("Binary");
      throw new UnsupportedOperationException("not supported binary messages");
    }

    @Override
    protected void onTextMessage(CharBuffer charBuffer) throws IOException {
      Gson gson = new Gson();
      String user = charBuffer.toString();
      //log.debug("Received message: {}", user);
      System.out.println("REcieved msg :"+user);
      PlayerData player = gson.fromJson(user, PlayerData.class);
      System.out.println("x:"+player.getX()+" y:"+player.getY());
      getWsOutbound().writeTextMessage(CharBuffer.wrap(gson.toJson(player)));
    }
  }
}
