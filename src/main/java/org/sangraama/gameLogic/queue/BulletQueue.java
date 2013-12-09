package org.sangraama.gameLogic.queue;

import org.sangraama.assets.Bullet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public enum BulletQueue {
    INSTANCE;
    private String TAG = "BulletQueue : ";
    private Marker subTile = MarkerFactory.getMarker("Bullet Queue: ");
    private Logger log = LoggerFactory.getLogger(BulletQueue.class);

    ConcurrentLinkedQueue<Bullet> newBulletQueue;
    ConcurrentLinkedQueue<Bullet> removeBulletQueue;

    private BulletQueue() {
        log.info(TAG, " Init Bullet queue ... ");
    }

    public void init(ConcurrentLinkedQueue<Bullet> newQueue, ConcurrentLinkedQueue<Bullet> removeQueue) {
        this.newBulletQueue = newQueue;
        this.removeBulletQueue = removeQueue;
    }

    public void addToBulletQueue(Bullet bullet) {
        try {
            this.newBulletQueue.add(bullet);
        } catch (Exception e) {
            log.error(TAG, e);
            e.printStackTrace();
        }
    }

    public void addToRemoveBulletQueue(Bullet bullet) {
        try {
            this.removeBulletQueue.add(bullet);
        } catch (Exception e) {
            log.error(TAG, e);
            e.printStackTrace();
        }
    }
}
