package org.sangraama.gameLogic;

import java.util.HashMap;
import java.util.Map;

import org.sangraama.assets.Player;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.thrift.assets.TPlayer;

public enum PassedPlayer {
	INSTANCE;

	private String TAG = "PassedPlayer :";

	private Map<Long, TPlayer> passdePlayers;

	private PassedPlayer() {
		this.passdePlayers = new HashMap<Long, TPlayer>();
	}

	public void addPassedPlayer(TPlayer p) {
		this.passdePlayers.put(p.getId(), p);
		System.out.println(TAG + "Added new passed player");
	}

	private Player fillPlayer(TPlayer tp, WebSocketConnection con) {
		SangraamaMap map = SangraamaMap.INSTANCE;
		Player p = new Player(tp.getId(), tp.getX() - map.getOriginX(),
				tp.getY() - map.getOriginY(), con);
		p.setV((float) tp.getV_x(), (float) tp.getV_y());

		return p;
	}

	public void redirectPassPlayerConnection(long userID,
			WebSocketConnection con) {
		if (!passdePlayers.isEmpty()) {
			TPlayer p = this.passdePlayers.get(userID);
			if (p != null) {
				Player player = fillPlayer(p, con);
				con.setPlayer(player);
				this.passdePlayers.remove(userID);
				System.out.println(TAG
						+ "Added passed player to GameEngine queue");
			}
		}
	}

}
