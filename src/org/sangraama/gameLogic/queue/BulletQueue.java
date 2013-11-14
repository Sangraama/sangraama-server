package org.sangraama.gameLogic.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.sangraama.assets.Bullet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

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
    
    public void init(ConcurrentLinkedQueue<Bullet> newQueue,ConcurrentLinkedQueue<Bullet> removeQueue) {
        this.newBulletQueue = newQueue;
        this.removeBulletQueue = removeQueue;
    }

    public void addToPlayerQueue(Bullet bullet) {
        try{
        this.newBulletQueue.add(bullet);
        }catch(Exception e){
            log.error(TAG, e);
            e.printStackTrace();
        }
    }

    public void addToRemovePlayerQueue(Bullet bullet) {
        try{
            this.removeBulletQueue.add(bullet);
            }catch(Exception e){
                log.error(TAG, e);
                e.printStackTrace();
            }
    }
}
