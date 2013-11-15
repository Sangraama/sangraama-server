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
    ConcurrentLinkedQueue<Player> defeatedPlayerQueue;
    
    private PlayerQueue() {
        log.info(TAG, " Init Player queue ... ");
    }
    
    public void init(ConcurrentLinkedQueue<Player> newQueue,ConcurrentLinkedQueue<Player> removeQueue,ConcurrentLinkedQueue<Player> defeatedQueue) {
        this.newPlayerQueue = newQueue;
        this.removePlayerQueue = removeQueue;
        this.defeatedPlayerQueue = defeatedQueue;
    }

    public void addToPlayerQueue(Player player) {
        try{
        this.newPlayerQueue.add(player);
        }catch(Exception e){
            log.error(TAG, e);
            e.printStackTrace();
        }
    }

    public void addToRemovePlayerQueue(Player removePlayer) {
        try{
            this.removePlayerQueue.add(removePlayer);
            }catch(Exception e){
                log.error(TAG, e);
                e.printStackTrace();
            }
    }
    
    public void addToDefaetList(Player player) {
        try{
            this.defeatedPlayerQueue.add(player);
            }catch(Exception e){
                log.error(TAG, e);
                e.printStackTrace();
            }
    }
}
