package org.sangraama.controller.clientprotocol;

import java.util.ArrayList;

import org.sangraama.coordination.staticPartition.TileCoordinator;

import com.google.gson.Gson;

/**
 * This class may completely replace by SangraamaTile class by setting a userId. Then send those as
 * a ArrayList
 * 
 * @author gihan
 * 
 */
public class TileInfo  extends SendProtocol{
    /* 11 : set the size of the tile */
    private String tiles;

    public TileInfo(long userID) {
        super(11, userID);
        this.tiles = new Gson().toJson(TileCoordinator.INSTANCE.getSubTilesCoordinations());
    }

    public TileInfo(long userID, ArrayList<SangraamaTile> tiles) {
        super(11, userID);
        this.tiles = new Gson().toJson(tiles);
    }

}
