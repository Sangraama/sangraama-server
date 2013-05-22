package org.sangraama.thrift.transmissionservice;

import org.apache.thrift.TException;
import org.sangraama.thrift.assets.Player;

public class PlayerTransmissionServiceImpl implements PlayerTransmissionService.Iface{

	@Override
	public void passPlayer(Player player) throws TException {
		System.out.println("New player!!"+player.getId());
		
	}

}
