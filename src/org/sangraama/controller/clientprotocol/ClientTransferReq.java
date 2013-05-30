package org.sangraama.controller.clientprotocol;

public class ClientTransferReq {

	private long userID = 0;
	private String newServerURL = "";
	private int newServerPort = 0;

	public ClientTransferReq(long userID, String newServerURL, int newServerPort) {
		this.userID = userID;
		this.newServerURL = newServerURL;
		this.newServerPort = newServerPort;
	}
}
