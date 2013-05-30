package org.sangraama.gameLogic;

import java.util.HashMap;
import java.util.Map;

import org.sangraama.asserts.Player;
import org.sangraama.controller.WebSocketConnection;

public enum PassedPlayer {
    INSTANCE;

    private GameEngine engine = null;
    private Map<Long, Player> passdePlayers = null;

    private PassedPlayer() {
        this.passdePlayers = new HashMap<Long,Player>();
        this.engine = GameEngine.INSTANCE;
    }

    public void addPassedPlayer(Player p) {
        this.passdePlayers.put(p.getUserID(), p);
    }

    public void redirectPassPlayerConnection(long userID, WebSocketConnection con) {
        if (!passdePlayers.isEmpty()) {
            this.engine.addToPlayerQueue(this.passdePlayers.get(userID));
            this.passdePlayers.remove(userID);
        }
    }

}
