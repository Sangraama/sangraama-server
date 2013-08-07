package org.sangraama.asserts.map.tileeditor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "map")
public class Map {
	@XmlAttribute(name="height")
	private int mapheight;
	@XmlAttribute(name="width")
	private int mapwidth;
	@XmlElement(name = "tileset")
	private List<TileSet> tilesetList = new ArrayList<TileSet>();
	@XmlElement(name = "layer")
	private List<Layer> layerList = new ArrayList<Layer>();
	@XmlElement(name = "objectgroup")
	private ObjectGroup objectGroup =new ObjectGroup();
	
	

	public ObjectGroup getObjectGroup() {
		return objectGroup;
	}

	public void setObjectGroup(ObjectGroup objectGroup) {
		this.objectGroup = objectGroup;
	}

	public int getMapheight() {
		return mapheight;
	}

	public void setMapheight(int mapheight) {
		this.mapheight = mapheight;
	}

	public int getMapwidth() {
		return mapwidth;
	}

	public void setMapwidth(int mapwidth) {
		this.mapwidth = mapwidth;
	}

	public List<Layer> getLayerList() {
		return layerList;
	}

	public void setLayerList(List<Layer> layerList) {
		this.layerList = layerList;
	}

	public List<TileSet> getTilesetList() {
		return tilesetList;
	}

	public void setTilesetList(List<TileSet> tilesetList) {
		this.tilesetList = tilesetList;
	}

}
