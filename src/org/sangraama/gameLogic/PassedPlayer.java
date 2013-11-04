package org.sangraama.gameLogic;

import java.util.HashMap;
import java.util.Map;

import org.sangraama.assets.Ship;
import org.sangraama.assets.SangraamaMap;
import org.sangraama.controller.WebSocketConnection;
import org.sangraama.thrift.assets.TPlayer;

/**
 * @deprecated due to there is no direct communication between servers
 * @author gihan
 *
 */
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

	/**
	 * @deprecated
	 * @param tp
	 * @param con
	 * @return
	 */
	private Ship fillPlayer(TPlayer tp, WebSocketConnection con) {
//		SangraamaMap map = SangraamaMap.INSTANCE;
//		//to be add w and h
//		Ship p = new Ship(tp.getId(), tp.getX() - map.getOriginX(),
//				tp.getY() - map.getOriginY(),0,0,0,0, con);
//		p.setV((float) tp.getV_x(), (float) tp.getV_y());

		return null;
	}
	
	/**
	 * @deprecated
	 * @param userID
	 * @param con
	 */
	public void redirectPassPlayerConnection(long userID,
			WebSocketConnection con) {
//		if (!passdePlayers.isEmpty()) {
//			TPlayer p = this.passdePlayers.get(userID);
//			if (p != null) {
//				Ship ship = fillPlayer(p, con);
//				con.setPlayer(ship);
//				this.passdePlayers.remove(userID);
//				System.out.println(TAG
//						+ "Added passed player to GameEngine queue");
//			}
//		}
	}

}
