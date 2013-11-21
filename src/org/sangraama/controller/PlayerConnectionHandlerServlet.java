package org.sangraama.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

@WebServlet("/sangraama/player")
public class PlayerConnectionHandlerServlet extends WebSocketServlet {
    public static final Logger log = LoggerFactory.getLogger(PlayerConnectionHandlerServlet.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected StreamInbound createWebSocketInbound(String str, HttpServletRequest req) {

        // log.info("Get new request to " + req.getServerName() + ":" + req.getServerPort());

        return new WebSocketConnection();
    }

    public void init(ServletConfig config) throws ServletException {
        System.out.println("initializing log4j");
        String log4jLocation = config.getInitParameter("log4j-properties-location");

        ServletContext sc = config.getServletContext();

        if (log4jLocation == null) {
            System.err
                    .println("*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        } else {
            String webAppPath = sc.getRealPath("/");
            String log4jProp = webAppPath + log4jLocation;
            File yoMamaYesThisSaysYoMama = new File(log4jProp);
            if (yoMamaYesThisSaysYoMama.exists()) {
                System.out.println("Initializing log4j with: " + log4jProp);
                PropertyConfigurator.configure(log4jProp);
            } else {
                System.err.println("*** " + log4jProp
                        + " file not found, so initializing log4j with BasicConfigurator");
                BasicConfigurator.configure();
            }
        }
        super.init(config);
    }
}
