package shapes;

import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
    private Ellipse2D ellipse;
    private int startX, startY;

    public GEllipse() {
        super(new Ellipse2D.Float(0, 0, 0, 0));
        this.ellipse = (Ellipse2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.ellipse.setFrame(x, y, 0, 0);
    }

    @Override
    public void dragPoint(int x, int y) {
        double minX = Math.min(startX, x);
        double minY = Math.min(startY, y);
        double width = Math.abs(x - startX);
        double height = Math.abs(y - startY);
        this.ellipse.setFrame(minX, minY, width, height);
    }

    @Override
    public void addPoint(int x, int y) {
        // Not needed for ellipse (2-point shape)
    }
}