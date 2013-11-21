package org.sangraama.asserts.map.tileeditor;

import javax.xml.bind.annotation.XmlAttribute;

public class TiledPolyline {
	@XmlAttribute(name="points")
	private String coordinates;
	
	public String getPoints() {
		return coordinates;
	}

	public void setPoints(String points) {
		this.coordinates = points;
	}
}
