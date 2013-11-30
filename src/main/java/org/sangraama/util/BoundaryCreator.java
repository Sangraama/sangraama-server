package org.sangraama.util;

import org.sangraama.assets.SangraamaMap;
import org.sangraama.assets.Wall;

import java.util.ArrayList;
import java.util.List;

public class BoundaryCreator {

    private SangraamaMap sangraamaMap;

    public BoundaryCreator(){
        sangraamaMap = SangraamaMap.INSTANCE;

    }
    public List<Wall> calculateWallBoundary() {
        List<Wall> wallList = new ArrayList<>();
        wallList.add(new Wall(0,0,sangraamaMap.getMaxWidth(),1));
        wallList.add(new Wall(0,0,1,sangraamaMap.getMaxHeight()));
        wallList.add(new Wall(sangraamaMap.getMaxWidth(),sangraamaMap.getMaxHeight(),sangraamaMap.getMaxWidth(),1));
        wallList.add(new Wall(sangraamaMap.getMaxWidth(),sangraamaMap.getMaxHeight(),1,sangraamaMap.getMaxHeight()));
        return wallList;
    }

}
