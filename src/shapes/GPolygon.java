package shapes;

import java.awt.Polygon;

public class GPolygon extends GShape {
    private Polygon polygon;

    public GPolygon() {
        super(new Polygon());
        this.polygon = (Polygon) this.getShape();
    }
    public void setPoint(int x, int y) {
        this.polygon.addPoint(x, y);
        this.polygon.addPoint(x, y);
    }
    public void dragPoint(int x, int y) {
        this.polygon.xpoints[this.polygon.npoints-1] = x;
        this.polygon.ypoints[this.polygon.npoints-1] = y;
    }
    @Override
    public void addPoint(int x, int y) {
        this.polygon.addPoint(x, y);
    }

    private int px, py;
    @Override
    public void setMovePoint(int x, int y) {
        // TODO Auto-generated method stub
        this.px = x;
        this.py = y;
    }
    @Override
    public void movePoint(int x, int y) {
        // TODO Auto-generated method stub
        int dx = x - px;
        int dy = y - py;

        for (int i=0; i<this.polygon.npoints; i++) {
            this.polygon.xpoints[i] += dx;
            this.polygon.ypoints[i] += dy;
        }

        this.px = x;
        this.py = y;
    }
}