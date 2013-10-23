package org.sangraama.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

@WebServlet("/sangraama/player")
public class PlayerConnectionHandlerServlet extends WebSocketServlet {
    private String TAG = "PlayerServlet :";

    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str, HttpServletRequest req) {

        System.out.println(TAG + " Get new request to " + req.getServerName() + ":"
                + req.getServerPort() + " str:" + str);

        return new WebSocketConnection();
    }
}
