package org.sangraama.gameLogic.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.sangraama.assets.DummyPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public enum DummyQueue {
    INSTANCE;
    private String TAG = "DummyQueue : ";
    private Marker subTile = MarkerFactory.getMarker("Dummy Queue: ");
    private Logger log = LoggerFactory.getLogger(DummyQueue.class);
    
    ConcurrentLinkedQueue<DummyPlayer> newDummyQueue;
    ConcurrentLinkedQueue<DummyPlayer> removeDummyQueue;
    
    private DummyQueue() {
        log.info(TAG, " Init Dummy queue ... ");
    }
    
    public void init(ConcurrentLinkedQueue<DummyPlayer> newQueue,ConcurrentLinkedQueue<DummyPlayer> removeQueue) {
        this.newDummyQueue = newQueue;
        this.removeDummyQueue = removeQueue;
    }

    public void addToDummyQueue(DummyPlayer dummyPlayer) {
        try{
        this.newDummyQueue.add(dummyPlayer);
        }catch(Exception e){
            log.error(TAG, e);
            e.printStackTrace();
        }
    }

    public void addToRemoveDummyQueue(DummyPlayer dummyPlayer) {
        try{
            this.removeDummyQueue.add(dummyPlayer);
            }catch(Exception e){
                log.error(TAG, e);
                e.printStackTrace();
            }
    }
}
