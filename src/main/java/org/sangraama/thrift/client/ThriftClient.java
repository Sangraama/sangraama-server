package org.sangraama.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.sangraama.coordination.ServerLocation;
import org.sangraama.thrift.assets.TPlayer;
import org.sangraama.thrift.transmissionservice.PlayerTransmissionService;

public class ThriftClient {
	TPlayer player=null;
	private String URL = "";
	private int port = 0;
	
	public  ThriftClient(TPlayer tPlayer,String url,int port) {
		this.player=tPlayer;
		this.URL = url;
		this.port =port;
	}
	
	public  ThriftClient(TPlayer tPlayer,ServerLocation serverLoc) {
        this.player=tPlayer;
        this.URL=serverLoc.getServerURL();
        this.port=serverLoc.getServerPort();
        //this.URL = url;
       // this.port =port;
    }
    public void invoke() {
	TTransport transport;
	transport = new TSocket(this.URL, port);
	TProtocol protocol = new TBinaryProtocol(transport);
	PlayerTransmissionService.Client client = new PlayerTransmissionService.Client(
		protocol);
	try {
	    transport.open();
	    // TPlayer player = new TPlayer(1000, 30, 40, 100, 100);

	    client.passPlayer(this.player);

	    transport.close();
	} catch (TTransportException e) {
	    e.printStackTrace();
	} catch (TException e) {
	    e.printStackTrace();
	}

    }

}
