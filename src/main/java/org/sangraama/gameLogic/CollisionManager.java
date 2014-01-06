package org.sangraama.gameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jbox2d.dynamics.contacts.Contact;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.DummyPlayer;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;
import org.sangraama.common.Constants;
import org.sangraama.gameLogic.queue.BulletQueue;
import org.sangraama.jsonprotocols.transfer.ScoreChangeTransferReq;
import org.slf4j.*;

public enum CollisionManager implements Runnable {
    INSTANCE;
    public static final Logger log = LoggerFactory.getLogger(CollisionManager.class);
    private GameEngine gameEngine;
    private BulletQueue bulletQueue;
    private volatile boolean isRun = true;
    private volatile boolean isUpdate = false;
    private List<Contact> collisionList;

    CollisionManager() {
        this.gameEngine = GameEngine.INSTANCE;
        this.bulletQueue = BulletQueue.INSTANCE;
        this.collisionList = new ArrayList<>();
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
        Timer timer = new Timer(Constants.simulatingDelay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                while (collisionList.size() > 0) {
                    processCollisions(collisionList.get(0));
                    collisionList.remove(0);
                }
            }
        });
        timer.start();

    }

    private void processCollisions(Contact collision) {
        // System.out.println(TAG + "Processing collisions ## ## ");
        if (collision.getFixtureA().getUserData().getClass() == Ship.class
                && collision.getFixtureB().getUserData().getClass() == Ship.class) {
            // System.out.println(TAG + "Ships are colliding.. " + i);
            Ship ship1 = (Ship) collision.getFixtureA().getUserData();
            Ship ship2 = (Ship) collision.getFixtureB().getUserData();
            processShipsCollision(ship1, ship2);

        } else if ((collision.getFixtureA().getUserData().getClass() == Ship.class && collision
                .getFixtureB().getUserData().getClass() == Bullet.class)
                || (collision.getFixtureA().getUserData().getClass() == Bullet.class && collision
                .getFixtureB().getUserData().getClass() == Ship.class)) {
            // System.out.println(TAG + "Hitting Bullet..");
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
            // System.out.println(TAG + "$$Hitting Island..");
            if (collision.getFixtureA().getUserData().getClass() == Ship.class) {
                ship = (Player) collision.getFixtureA().getUserData();
            } else {
                ship = (Player) collision.getFixtureB().getUserData();
            }
            reduceShipHealth(ship.getUserID(), (float) -0.1);
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
            this.bulletQueue.addToRemoveBulletQueue(bullet);
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
            this.bulletQueue.addToRemoveBulletQueue(bullet);
        } else if (collision.getFixtureA()
                .getUserData().getClass() == Bullet.class && collision.getFixtureB()
                .getUserData().getClass() == Bullet.class) {
            Bullet bulletA;
            Bullet bulletB;
            bulletA = (Bullet) collision.getFixtureA().getUserData();
            bulletB = (Bullet) collision.getFixtureB().getUserData();
            this.bulletQueue.addToRemoveBulletQueue(bulletA);
            this.bulletQueue.addToRemoveBulletQueue(bulletB);
        }

    }

    private void processShipsCollision(Ship... ships) {
        for (Ship ship : ships) {
            reduceShipHealth(ship.getUserID(), -0.2f);
        }
    }

    private void processBulletShipCollition(Player ship, Bullet bullet) {
        boolean playerInServer = false;
        reduceShipHealth(ship.getUserID(), -1f);
        long shooterUserID = bullet.getPlayerId();
        for (Player player : gameEngine.getPlayerList()) {
            if (player.getUserID() == shooterUserID) {
                player.setScore(10);
                playerInServer = true;
                break;
            }
        }
        this.bulletQueue.addToRemoveBulletQueue(bullet);
        if (!playerInServer) {
            sendScoreChangeEventFromDummy(shooterUserID, 10);
        }
    }

    private void reduceShipHealth(float userID, float valChange) {
        for (Player player : gameEngine.getPlayerList()) {
            if (player.getUserID() == userID) {
                player.setHealth(valChange);
                break;
            }
        }
    }

    private void sendScoreChangeEventFromDummy(long shipID, int scoreChange) {
        List<DummyPlayer> dummyList = AOIEngine.INSTANCE.getDummyList();
        for (DummyPlayer dummyPlayer : dummyList) {
            if (dummyPlayer.getUserID() == shipID) {
                ScoreChangeTransferReq scoreChangeReq = new ScoreChangeTransferReq(21, shipID,
                        scoreChange);
                dummyPlayer.sendScoreChange(scoreChangeReq);
            }
        }
    }
}
