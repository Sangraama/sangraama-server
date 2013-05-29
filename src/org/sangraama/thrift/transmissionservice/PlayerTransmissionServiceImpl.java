package org.sangraama.thrift.transmissionservice;

import org.apache.thrift.TException;
import org.sangraama.asserts.Player;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.thrift.assets.TPlayer;

public class PlayerTransmissionServiceImpl implements PlayerTransmissionService.Iface{

    @Override
    public void passPlayer(TPlayer player) throws TException {
        System.out.println("New player from other server.. ID = "+player.getId());
        
        GameEngine gameEngine=GameEngine.INSTANCE;
        gameEngine.addToPlayerQueue(fillPlayer(player));
    }
    
    private Player fillPlayer(TPlayer tp){
        Player p = new Player(tp.getId(), null);
        p.setV((float)tp.getV_x(), (float)tp.getV_y());
        p.setX(tp.getX());
        p.setY(tp.getY());
        
        return p;
    }

}
