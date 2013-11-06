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

    /**
     * This method is used to pass the bullet to the suitable neighbor server if bullet is out of
     * the area of the current server.
     * 
     * @param bullet
     *            bullet which going to transferred to another server
     */
    public void passBullets(Bullet bullet) {
        String newHost = (String) TileCoordinator.INSTANCE.getSubTileHost(bullet.getX(),
                bullet.getY());
        BulletTransferReq bulletTransReq = new BulletTransferReq(20, bullet.getPlayerId(),
                bullet.getId(), bullet.getX(), bullet.getY(), bullet.getVelocity(),
                bullet.getOriginX(), bullet.getOriginY(), bullet.getScreenHeight(),
                bullet.getScreenWidth(), newHost, bullet.getType());
        gameEngine.addToRemoveBulletQueue(bullet);
        passBulletInfoToClient(bullet, bulletTransReq);
    }

    private void passBulletInfoToClient(Bullet bullet, BulletTransferReq bulletTransReq) {
        for (Player player : gameEngine.getPlayerList()) {
            if (player.getUserID() == bullet.getPlayerId()) {
                player.sendTransferringGameObjectInfo(bulletTransReq);
            }
        }
    }
}
