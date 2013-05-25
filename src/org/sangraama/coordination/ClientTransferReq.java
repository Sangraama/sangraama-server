package org.sangraama.coordination;

public class ClientTransferReq {

	private int userID = 0;
	private String newServerURL = "";
	private int newServerPort = 0;

	public ClientTransferReq(int userID, String newServerURL, int newServerPort) {
		this.userID = userID;
		this.newServerURL = newServerURL;
		this.newServerPort = newServerPort;
	}
}
