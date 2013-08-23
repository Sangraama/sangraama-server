package org.sangraama.gameLogic;

import org.jbox2d.dynamics.contacts.Contact;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;

public enum CollisionManager implements Runnable {
    INSTANCE;
    // Debug
    private String TAG = "Collision Manager :";

    private volatile boolean isRun = true;

    private Contact collisions;
    private GameEngine gameEngine;
    private volatile boolean isUpdate = false;

    CollisionManager() {
        this.gameEngine = GameEngine.INSTANCE;
        System.out.println(TAG + " init ... ");
    }

    public synchronized boolean setStop() {
        this.isRun = false;
        return this.isRun;
    }

    public synchronized void setCollisionList(Contact col) {
        this.collisions = col;
        this.isUpdate = true;
    }

    @Override
    public void run() {
        while (this.isRun) {
            if (isUpdate) {
                Contact colList = this.collisions;
                do {
                    this.processCollisions(colList);
                } while (colList.getNext() != null);
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
        if (collision.getFixtureA().getUserData().getClass() == Ship.class
                && collision.getFixtureB().getUserData().getClass() == Ship.class) {
            System.out.println("Ships are colliding.. " + i);
            processShipsCollision();

        } else if ((collision.getFixtureA().getUserData().getClass() == Ship.class && collision
                .getFixtureB().getUserData().getClass() == Bullet.class)
                || (collision.getFixtureA().getUserData().getClass() == Bullet.class && collision
                        .getFixtureB().getUserData().getClass() == Ship.class)) {
            System.out.println("Hittn Bullet..");
            if (collision.getFixtureA().getUserData().getClass() == Ship.class) {
                Player ship = (Player) collision.getFixtureA().getUserData();
                Bullet bullet = (Bullet) collision.getFixtureB().getUserData();
                processBulletShipCollition(ship, bullet);
            } else {
                Player ship = (Player) collision.getFixtureB().getUserData();
                Bullet bullet = (Bullet) collision.getFixtureA().getUserData();
                processBulletShipCollition(ship, bullet);
            }
        }
    }

    private void processShipsCollision() {
        // Code to process when ships are collided.
    }

    private void processBulletShipCollition(Player ship, Bullet bullet) {
        System.out.println("Victime ship : " + ship.getUserID());
        //System.out.println("Shooter ship : " + bullet.getPlayerId());
        // this.gameEngine.removeBullet(bullet);
    }
}
