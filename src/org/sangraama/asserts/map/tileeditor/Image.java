package org.sangraama.asserts.map.tileeditor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Image {
	
	@XmlAttribute(required = true)
private String source;
	@XmlAttribute(required = true)
private int width;
	@XmlAttribute(required = true)
private int height;
	
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public int getWidth() {
	return width;
}
public void setWidth(int width) {
	this.width = width;
}
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}


}
