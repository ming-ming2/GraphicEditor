package shapes;

import java.awt.geom.Arc2D;

public class GArc extends GShape {
    private Arc2D arc;
    private int startAngle = 0;
    private int arcAngle = 90;

    public GArc() {
        super(new Arc2D.Float(0, 0, 0, 0, 0, 90, Arc2D.PIE));
        this.arc = (Arc2D) this.getShape();
    }

    public GArc(int type) {
        super(new Arc2D.Float(0, 0, 0, 0, 0, 90, type));
        this.arc = (Arc2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.arc.setArc(x, y, 0, 0, startAngle, arcAngle, arc.getArcType());
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = arc.getX();
        double oy = arc.getY();
        double w = x - ox;
        double h = y - oy;
        this.arc.setArc(ox, oy, w, h, startAngle, arcAngle, arc.getArcType());
    }

    @Override
    public void addPoint(int x, int y) {
    }

    public void setAngles(int startAngle, int arcAngle) {
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        double x = arc.getX();
        double y = arc.getY();
        double w = arc.getWidth();
        double h = arc.getHeight();
        this.arc.setArc(x, y, w, h, startAngle, arcAngle, arc.getArcType());
    }

    public void setArcType(int type) {
        double x = arc.getX();
        double y = arc.getY();
        double w = arc.getWidth();
        double h = arc.getHeight();
        this.arc.setArc(x, y, w, h, startAngle, arcAngle, type);
    }
}