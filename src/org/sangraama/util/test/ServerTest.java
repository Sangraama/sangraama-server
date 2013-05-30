package org.sangraama.util.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ServerTest")
public class ServerTest extends HttpServlet{

	
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	int serverPort=0, thriftServerPort=0;
	String serverName=null;
	Properties prop;
	response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<title>Server Test</title>" +
       "<body bgcolor=FFFFFF>");

    out.println("<h2>Server started</h2>");
    serverPort=request.getServerPort();
    serverName=request.getServerName();
    prop = new Properties();
    prop.load(getClass().getResourceAsStream("/sangraamaserver.properties"));
    thriftServerPort=Integer.parseInt(prop.getProperty("thriftserverport"));
    
    out.println("<p>Server Port :"+" "+serverPort+ "</p>");
    out.println("<p>Server Name :"+" "+serverName+ "</p>");
    out.println("<p>Thrift Server Port :"+" "+thriftServerPort+ "</p>");
      out.close();	
	}
}
