package org.sangraama.gameLogic;

import java.util.concurrent.BlockingQueue;

import org.sangraama.assets.AbsPlayer;

public class UpdateEngine extends Thread {
    private volatile boolean isRun = true;
    private BlockingQueue<AbsPlayer> sendPlayerQueue;

    public UpdateEngine(BlockingQueue<AbsPlayer> sendPlayerQueue) {
        this.sendPlayerQueue = sendPlayerQueue;
    }

    public void run() {
        while (this.isRun) {
            try {
                AbsPlayer abs = this.sendPlayerQueue.take();
                abs.sendUpdate(abs.getDeltaList());
                abs.setDeltaList(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStop() {
        this.isRun = false;
    }

}
