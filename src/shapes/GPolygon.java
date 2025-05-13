package shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GPolygon extends GShape {
    private Polygon polygon;

    public GPolygon(){
        super(new Polygon());
        this.polygon = (Polygon) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.polygon.addPoint(x, y);
        this.polygon.addPoint(x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        this.polygon.xpoints[this.polygon.npoints-1] = x;
        this.polygon.ypoints[this.polygon.npoints-1] = y;
    }

    @Override
    public void addPoint(int x, int y) {
        this.polygon.addPoint(x, y);
    }
}
