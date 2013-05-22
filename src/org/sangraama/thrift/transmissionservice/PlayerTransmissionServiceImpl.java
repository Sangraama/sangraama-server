package org.sangraama.thrift.transmissionservice;

import org.apache.thrift.TException;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.assets.Player;

public class PlayerTransmissionServiceImpl implements PlayerTransmissionService.Iface{

	@Override
	public void passPlayer(Player player) throws TException {
		System.out.println("New player!!"+player.getId());
		GameEngine gameEngine=GameEngine.INSTANCE;
		//gameEngine.addToPlayerQueue(player);
	}

}
