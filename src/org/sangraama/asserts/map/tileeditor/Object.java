package org.sangraama.asserts.map.tileeditor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Object {
    @XmlAttribute(name = "x")
    private int x;
    @XmlAttribute(name = "y")
    private int y;
    @XmlAttribute(name = "width")
    private int width;
    @XmlAttribute(name = "height")
    private int height;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "polygon")
    private TiledPolygon polygon = new TiledPolygon();
    @XmlElement(name = "polyline")
    private TiledPolyline polyline = new TiledPolyline();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public TiledPolyline getPolyline() {
        return polyline;
    }

    public void setPolyline(TiledPolyline polyline) {
        this.polyline = polyline;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public TiledPolygon getPolygon() {
        return polygon;
    }

    public void setPolygon(TiledPolygon polygon) {
        this.polygon = polygon;
    }
}
