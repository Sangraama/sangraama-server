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
public class TileInfo {
    /* 11 : set the size of the tile */
    private int type = 11;
    private long userId;
    private String tiles;

    public TileInfo(long userId) {
        this.userId = userId;
        this.tiles = new Gson().toJson(TileCoordinator.INSTANCE.getSubTilesCoordinations());
    }

    public TileInfo(long userId, ArrayList<SangraamaTile> tiles) {
        this.userId = userId;
        this.tiles = new Gson().toJson(tiles);
    }

}
