package org.sangraama.gameLogic;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.dynamics.contacts.Contact;
import org.sangraama.assets.AbsPlayer;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.controller.clientprotocol.ScoreChangeTransferReq;

public enum CollisionManager implements Runnable {
    INSTANCE;
    // Debug
    private String TAG = "Collision Manager :";
    private GameEngine gameEngine;
    private volatile boolean isRun = true;
    private volatile boolean isUpdate = false;
    private List<Contact> collisionList;

    CollisionManager() {
        gameEngine = GameEngine.INSTANCE;
        collisionList = new ArrayList<>();
        System.out.println(TAG + " init ... ");
    }

    public synchronized boolean setStop() {
        this.isRun = false;
        return this.isRun;
    }

    public synchronized void addToCollisionList(Contact col) {
        this.collisionList.add(col);
        this.isUpdate = true;
    }

    @Override
    public void run() {
        while (this.isRun) {
            if (this.isUpdate) {
                while (this.collisionList.size() > 0) {
                    this.processCollisions(this.collisionList.get(0));
                    this.collisionList.remove(0);
                }
                this.isUpdate = false;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processCollisions(Contact collision) {
        int i = 0;
        System.out.println(TAG + "Processing collisions ## ## ");
        if (collision.getFixtureA().getUserData().getClass() == Ship.class
                && collision.getFixtureB().getUserData().getClass() == Ship.class) {
            System.out.println(TAG + "Ships are colliding.. " + i);
            Ship ship1 = (Ship) collision.getFixtureA().getUserData();
            Ship ship2 = (Ship) collision.getFixtureB().getUserData();
            processShipsCollision(ship1, ship2);

        } else if ((collision.getFixtureA().getUserData().getClass() == Ship.class && collision
                .getFixtureB().getUserData().getClass() == Bullet.class)
                || (collision.getFixtureA().getUserData().getClass() == Bullet.class && collision
                        .getFixtureB().getUserData().getClass() == Ship.class)) {
            System.out.println(TAG + "Hitting Bullet..");
            if (collision.getFixtureA().getUserData().getClass() == Ship.class) {
                Player ship = (Player) collision.getFixtureA().getUserData();
                Bullet bullet = (Bullet) collision.getFixtureB().getUserData();
                processBulletShipCollition(ship, bullet);
            } else {
                Player ship = (Player) collision.getFixtureB().getUserData();
                Bullet bullet = (Bullet) collision.getFixtureA().getUserData();
                processBulletShipCollition(ship, bullet);
            }
        } else if (("island".equals(collision.getFixtureA().getUserData()) && collision
                .getFixtureB().getUserData().getClass() == Ship.class)
                || ("island".equals(collision.getFixtureB().getUserData()) && collision
                        .getFixtureA().getUserData().getClass() == Ship.class)) {
            Player ship;
            System.out.println(TAG + "$$Hitting Island..");
            if (collision.getFixtureA().getUserData().getClass() == Ship.class) {
                ship = (Player) collision.getFixtureA().getUserData();
            } else {
                ship = (Player) collision.getFixtureB().getUserData();
            }
            reduceShipHealth(ship.getUserID(), (float) -0.5);
        } else if (("island".equals(collision.getFixtureA().getUserData()) && collision
                .getFixtureB().getUserData().getClass() == Bullet.class)
                || ("island".equals(collision.getFixtureB().getUserData()) && collision
                        .getFixtureA().getUserData().getClass() == Bullet.class)) {
            Bullet bullet;
            if (collision.getFixtureA().getUserData().getClass() == Ship.class) {
                bullet = (Bullet) collision.getFixtureA().getUserData();
            } else {
                bullet = (Bullet) collision.getFixtureB().getUserData();
            }
            gameEngine.addToRemoveBulletQueue(bullet);
        } else if (("wall".equals(collision.getFixtureA().getUserData()) && collision.getFixtureB()
                .getUserData().getClass() == Bullet.class)
                || ("wall".equals(collision.getFixtureB().getUserData()) && collision.getFixtureA()
                        .getUserData().getClass() == Bullet.class)) {
            Bullet bullet;
            if (collision.getFixtureA().getUserData().getClass() == Ship.class) {
                bullet = (Bullet) collision.getFixtureA().getUserData();
            } else {
                bullet = (Bullet) collision.getFixtureB().getUserData();
            }
            gameEngine.addToRemoveBulletQueue(bullet);
        }
    }

    private void processShipsCollision(Ship... ships) {
        for (Ship ship : ships) {
            reduceShipHealth(ship.getUserID(), -10);
        }
    }

    private void processBulletShipCollition(Player ship, Bullet bullet) {
        boolean playerInServer = false;
        reduceShipHealth(ship.getUserID(), -10);
        long shooterUserID = bullet.getPlayerId();
        for (Player player : gameEngine.getPlayerList()) {
            if (player.getUserID() == shooterUserID) {
                player.setScore(10);
                playerInServer = true;
                break;
            }
        }
        gameEngine.addToRemoveBulletQueue(bullet);
        if(!playerInServer){
            sendScoreChangeEventFromDummy(shooterUserID, 10);
        }
    }

    private void reduceShipHealth(float userID, float valChange) {
        System.out.println(TAG + "PlayerList size : " + gameEngine.getPlayerList().size());
        for (Player player : gameEngine.getPlayerList()) {
            if (player.getUserID() == userID) {
                player.setHealth(valChange);
                break;
            }
        }
    }
    
    private void sendScoreChangeEventFromDummy(long shipID,int scoreChange){
        List<DummyPlayer> dummyList = UpdateEngine.INSTANCE.getDummyList();
        for(DummyPlayer dummyPlayer : dummyList){
            if(dummyPlayer.getUserID() == shipID){
                ScoreChangeTransferReq scoreChangeReq = new ScoreChangeTransferReq(21, shipID, scoreChange);
                dummyPlayer.sendScoreChange(scoreChangeReq);
            }
        }
    }
}
