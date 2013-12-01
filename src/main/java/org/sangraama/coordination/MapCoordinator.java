package org.sangraama.coordination;

import org.sangraama.jsonprotocols.send.SangraamaTile;

import java.util.List;

public interface MapCoordinator {
    public void init();

    public void generateSubTiles();

    public String getSubTileHost(float x, float y);

    public List<SangraamaTile> getSubTilesCoordinations();
}
