package shapes;

import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
    private Ellipse2D ellipse;

    public GEllipse() {
        super(new Ellipse2D.Float(0, 0, 0, 0));
        this.ellipse = (Ellipse2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.ellipse.setFrame(x, y, 0, 0);
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = ellipse.getX();
        double oy = ellipse.getY();
        double w = x - ox;
        double h = y - oy;
        this.ellipse.setFrame(ox, oy, w, h);
    }

    @Override
    public void addPoint(int x, int y) {
        // Not needed for ellipse (2-point shape)
    }
}