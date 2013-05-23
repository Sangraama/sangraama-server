package org.sangraama.controller;

import java.util.ArrayList;

import org.sangraama.asserts.Player;
import org.sangraama.thrift.assets.TPlayer;
import org.sangraama.thrift.client.ThriftClient;

public enum PlayerConnectionHandler implements Runnable {
    INSTANCE;
    private ArrayList<Player> passPlayerList = null;
    private boolean isPass = false;
    
    public void setServerHandler(){
        
    }
    
    public void callThriftServer() {
        Player player= null;
     TPlayer tPlayer=new TPlayer();
     if(!passPlayerList.isEmpty()){
          player=passPlayerList.get(0);
         passPlayerList.remove(0);
     }
     tPlayer.id=player.getUserID();
     tPlayer.x=(int) player.getX();
     tPlayer.y=(int) player.getY();
     tPlayer.v_x=player.getV().x;
     tPlayer.v_y=player.getV().y;
        //ThriftClient thriftClient= new ThriftClient();
        //s thriftClient.invoke();
    }

    public void setPassPlayer(Player player) {

        this.passPlayerList.add(player);
        isPass = true;
        
    }

    public void run() {
        while (true)
            if (isPass) {
                
            }

    }
    
}
