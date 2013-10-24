package org.sangraama.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

public class DummyPlayerConnection extends WebSocketServlet {
    private String TAG = "DummyPlayerServlet :";

    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str, HttpServletRequest req) {

        System.out.println(TAG + " get new request to " + req.getServerName() + ":"
                + req.getServerPort());

        return new DummyWebScocketConnection();
    }
}
