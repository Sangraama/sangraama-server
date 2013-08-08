package org.sangraama.thrift.transmissionservice;

import org.apache.thrift.TException;
import org.sangraama.assets.Ship;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.gameLogic.PassedPlayer;
import org.sangraama.thrift.assets.TPlayer;

public class PlayerTransmissionServiceImpl implements PlayerTransmissionService.Iface {

    @Override
    public void passPlayer(TPlayer tPlayer) throws TException {
        System.out.println("New player from other server.. ID = " + tPlayer.getId());

        GameEngine gameEngine = GameEngine.INSTANCE;
        //gameEngine.addToPlayerQueue(fillPlayer(tPlayer));
        PassedPlayer.INSTANCE.addPassedPlayer(tPlayer);
    }

}
