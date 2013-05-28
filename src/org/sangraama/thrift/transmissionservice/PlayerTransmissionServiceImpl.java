package org.sangraama.thrift.transmissionservice;

import org.apache.thrift.TException;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.assets.TPlayer;

public class PlayerTransmissionServiceImpl implements PlayerTransmissionService.Iface{

    @Override
    public void passPlayer(TPlayer player) throws TException {
        System.out.println("New player from other server.. ID = "+player.getId());
        GameEngine gameEngine=GameEngine.INSTANCE;
        //gameEngine.addToPlayerQueue(player);
        
    }

}
