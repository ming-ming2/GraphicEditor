package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape {
    private Rectangle2D rectangle;
    private int startX, startY;

    public GRectangle(){
        super(new Rectangle2D.Float(0,0,0,0));
        this.rectangle = (Rectangle2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.rectangle.setFrame(x, y, 0, 0);
    }

    @Override
    public void dragPoint(int x, int y) {
        double minX = Math.min(startX, x);
        double minY = Math.min(startY, y);
        double width = Math.abs(x - startX);
        double height = Math.abs(y - startY);
        this.rectangle.setFrame(minX, minY, width, height);
    }

    @Override
    public void addPoint(int x, int y) {
        //
    }
}