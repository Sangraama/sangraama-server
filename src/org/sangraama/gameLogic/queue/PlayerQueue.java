package org.sangraama.gameLogic.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.sangraama.assets.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public enum PlayerQueue {
    INSTANCE;
    private String TAG = "PlayerQueue : ";
    private Marker subTile = MarkerFactory.getMarker("Player Queue : ");
    private Logger log = LoggerFactory.getLogger(PlayerQueue.class);
    
    ConcurrentLinkedQueue<Player> newPlayerQueue;
    ConcurrentLinkedQueue<Player> removePlayerQueue;
    
    private PlayerQueue() {
        log.info(TAG, " Init Player queue ... ");
    }
    
    public void init(ConcurrentLinkedQueue<Player> newQueue,ConcurrentLinkedQueue<Player> removeQueue) {
        this.newPlayerQueue = newQueue;
        this.removePlayerQueue = removeQueue;
    }

    public void addToPlayerQueue(Player ship) {
        try{
        this.newPlayerQueue.add(ship);
        }catch(Exception e){
            log.error(TAG, e);
            e.printStackTrace();
        }
    }

    public void addToRemovePlayerQueue(Player ship) {
        try{
            this.removePlayerQueue.add(ship);
            }catch(Exception e){
                log.error(TAG, e);
                e.printStackTrace();
            }
    }
}
