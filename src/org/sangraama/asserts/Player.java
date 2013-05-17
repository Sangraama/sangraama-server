package org.sangraama.asserts;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.jbox2d.dynamics.World;
import org.sangraama.common.Constants;
import org.sangraama.controller.EventHandler;
import org.sangraama.gameLogic.GameWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet("/org/sangraama/asserts/Player")
public class Player extends WebSocketServlet {

  // private static LoggerFactory.getLogger Logger log = (EventHandler. class );
  private static Logger log = LoggerFactory.getLogger(EventHandler.class);
  GameWorld sangraamaWorld=GameWorld.getInstance();
  

  public Player() {
    
  }

  public void init(){
	  
	  new Thread(sangraamaWorld).start();
	  System.out.println("Simulating world");
	  
  }
  @Override
  protected StreamInbound createWebSocketInbound(String arg0,
      HttpServletRequest arg1) {
    System.out.println("Trigger createWebSocketInBound");
    return new WebSocketConnection();
  }

  private static class WebSocketConnection extends MessageInbound {

	  GameWorld sangraamaWorld=GameWorld.getInstance();
	  
    @Override
    protected void onOpen(WsOutbound outbound) {
      log.info("Open connection");
      System.out.println("Open Connection");
    }

    @Override
    protected void onClose(int status) {
      // log.info("Conne            ction closed");
      System.out.println("Close connection");
      
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
      int id;
      Random rdmGen=new Random();
      String recievedMsg = charBuffer.toString();
      if("Start".equals(recievedMsg)){    	  
    	  id=rdmGen.nextInt(100);
    	  sangraamaWorld.addPlayer(id);
    	  System.out.println("#########");
    	  sangraamaWorld.stopSimulating();
      }
    }
  }
  

}
