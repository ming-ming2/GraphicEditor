package shapes;

import java.awt.geom.RoundRectangle2D;

public class GRoundRectangle extends GShape {
    private RoundRectangle2D roundRect;
    private double arcWidth = 20.0;
    private double arcHeight = 20.0;

    public GRoundRectangle() {
        super(new RoundRectangle2D.Float(0, 0, 0, 0, 20, 20));
        this.roundRect = (RoundRectangle2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.roundRect.setRoundRect(x, y, 0, 0, arcWidth, arcHeight);
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = roundRect.getX();
        double oy = roundRect.getY();
        double w = x - ox;
        double h = y - oy;
        this.roundRect.setRoundRect(ox, oy, w, h, arcWidth, arcHeight);
    }

    @Override
    public void addPoint(int x, int y) {
    }

    public void setArcSize(double arcWidth, double arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        double x = roundRect.getX();
        double y = roundRect.getY();
        double w = roundRect.getWidth();
        double h = roundRect.getHeight();
        this.roundRect.setRoundRect(x, y, w, h, arcWidth, arcHeight);
    }
}