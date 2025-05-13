package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape {
    private Rectangle2D rectangle;
    public GRectangle(){
        super(new Rectangle2D.Float(0,0,0,0));
        this.rectangle = (Rectangle2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.rectangle.setFrame(x,y,0,0);
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = rectangle.getX();
        double oy = rectangle.getY();
        double w = x-ox;
        double h = y-oy;
        this.rectangle.setFrame(ox,oy,w,h);
    }

    @Override
    public void addPoint(int x, int y) {
        //
    }
}
