package org.sangraama.jsonprotocols.transfer;

import com.google.gson.Gson;
import org.sangraama.jsonprotocols.SendProtocol;
import org.sangraama.util.SignMsg;

public class ScoreChangeTransferReq extends SendProtocol {

    private String info;
    private byte[] signedInfo;

    public ScoreChangeTransferReq(long userID, float scoreChange) {
        super(21, userID);
        ScoreChangeInfo scoreChangeInfo = new ScoreChangeInfo(userID, scoreChange);
        Gson gson = new Gson();
        info = gson.toJson(scoreChangeInfo);
        signedInfo = SignMsg.INSTANCE.signMessage(info);
    }

    public float getScore(String info) {
        Gson gson = new Gson();
        ScoreChangeInfo scoreChangeInfo = gson.fromJson(info, ScoreChangeInfo.class);
        return scoreChangeInfo.scoreChange;
    }

    private class ScoreChangeInfo {

        private float scoreChange;
        private long userID;

        public ScoreChangeInfo(long userID, float scoreChange) {
            this.userID = userID;
            this.scoreChange = scoreChange;
        }
    }
}