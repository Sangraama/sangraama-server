package org.sangraama.asserts.map.tileeditor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class TileSet {
	 @XmlAttribute(required = true)
private int firstgid;
	 @XmlAttribute(required = true)
private String name;
	 @XmlAttribute(required = true)
private int tilewidth;
	 @XmlAttribute(required = true)
private int tileheight;

	 @XmlElement(name="image")
	 private Image image = new Image();
	 
public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

public int getFirstgid() {
		return firstgid;
	}

	public void setFirstgid(int firstgid) {
		this.firstgid = firstgid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTilewidth() {
		return tilewidth;
	}

	public void setTilewidth(int tilewidth) {
		this.tilewidth = tilewidth;
	}

	public int getTileheight() {
		return tileheight;
	}

	public void setTileheight(int tileheight) {
		this.tileheight = tileheight;
	}

	



}
