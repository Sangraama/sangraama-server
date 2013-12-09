package org.sangraama.controller;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class DummyPlayerConnection extends WebSocketServlet {
    public static final Logger log = LoggerFactory.getLogger(DummyPlayerConnection.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str, HttpServletRequest req) {

        log.info("get new request to " + req.getServerName() + ":"
                + req.getServerPort());

        return new DummyWebScocketConnection();
    }
}
