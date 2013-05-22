package org.sangraama.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.sangraama.thrift.assets.Player;
import org.sangraama.thrift.transmissionservice.PlayerTransmissionService;

public class ThriftClient {
    private void invoke() {
	TTransport transport;
	transport = new TSocket("localhost", 7912);
	TProtocol protocol = new TBinaryProtocol(transport);
	PlayerTransmissionService.Client client = new PlayerTransmissionService.Client(
		protocol);
	try {
	    transport.open();
	    Player player = new Player(1000, 30, 40, 100, 100);

	    client.passPlayer(player);

	    transport.close();
	} catch (TTransportException e) {
	    e.printStackTrace();
	} catch (TException e) {
	    e.printStackTrace();
	}

    }

}
