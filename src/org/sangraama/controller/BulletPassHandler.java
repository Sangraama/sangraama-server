package org.sangraama.controller;

import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.controller.clientprotocol.BulletTransferReq;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.GameEngine;

public enum BulletPassHandler {

    INSTANCE;

    private GameEngine gameEngine;

    private BulletPassHandler() {
        this.gameEngine = GameEngine.INSTANCE;
    }

    public void passBullets(Bullet bullet) {
        String newHost = (String) TileCoordinator.INSTANCE.getSubTileHost(bullet.getX(),
                bullet.getY());
        BulletTransferReq bulletTransReq = new BulletTransferReq(33, bullet.getPlayerId(),
                bullet.getId(), bullet.getX(), bullet.getY(), newHost);
        gameEngine.removeBullet(bullet);
        passBulletInfoToClient(bullet,bulletTransReq);
    }
    
    private void passBulletInfoToClient(Bullet bullet, BulletTransferReq bulletTransReq){
        for(Player player : gameEngine.getPlayerList()){
            if(player.getUserID() == bullet.getPlayerId()){
                player.sendPassGameObjectInfo(bulletTransReq);
            }
        }
    }
}
