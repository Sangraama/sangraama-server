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
    private String TAG = "PlayerServlet :";
    
    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str,
            HttpServletRequest req) {

        switch (str) {
        case "NewPlayer":
            System.out.println(TAG + "Trigger newplayer");
            Random r = new Random();
            WebSocketConnection con = new WebSocketConnection();

            System.out.println(TAG + "New Player creating");
            Player player = new Player(r.nextInt(1000), con);
            System.out.println(TAG + "Player created");
            con.setPlayer(player);
            return con;

        case "PassPlayer":
            System.out.println(TAG + "Trigger passplayer");
            WebSocketConnection ncon = new WebSocketConnection();

            System.out.println(TAG + "WebSocket created for pass player");
            return ncon;
        default:
            return null;
        }

    }

    @Override
    protected String selectSubProtocol(List<String> subProtocols) {
        return subProtocols.get(0);
    }
}
