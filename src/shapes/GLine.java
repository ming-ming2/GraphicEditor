package shapes;

import java.awt.geom.Line2D;

public class GLine extends GShape {
    private Line2D line;

    public GLine() {
        super(new Line2D.Float(0, 0, 0, 0));
        this.line = (Line2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.line.setLine(x, y, x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        double x1 = line.getX1();
        double y1 = line.getY1();
        this.line.setLine(x1, y1, x, y);
    }

    @Override
    public void addPoint(int x, int y) {
    }
}