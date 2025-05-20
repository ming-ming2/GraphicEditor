package transformers;

import global.GConstants;
import shapes.GShape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static global.GConstants.*;

public class GResizer extends GTransformer {
    int px, py;
    int cx,cy;

    private Rectangle2D bounds;
    private EAnchor eResizeAnchor;

    public GResizer(GShape shape) {
        super(shape);
        this.shape = shape;
        this.bounds = shape.getBounds();
    }

    @Override
    public void start(int x, int y) {
        px = x;
        py = y;
        Rectangle r = (Rectangle) shape.getBounds();

        EAnchor eSelectedAnchor = shape.getESelectedAnchor();
        switch (eSelectedAnchor) {
            case eNW -> {
                eResizeAnchor = EAnchor.eSE;
                cx=r.x+r.width;
                cy=r.y+r.height;
            }
            case eWW -> {
                eResizeAnchor = EAnchor.eEE;
                cx=r.x+ r.width;
                cy=r.y+r.height/2;
            }
            case eSW -> {
                eResizeAnchor = EAnchor.eNE;
                cx=r.x;
                cy=r.y;
            }
            case eSS -> {
                eResizeAnchor = EAnchor.eNN;
                cx=r.x+r.width;
                cy=r.y;
            }
            case eSE -> {
                eResizeAnchor = EAnchor.eNW;
                cx=r.x;
                cy=r.y;
            }
            case eEE -> {
                eResizeAnchor = EAnchor.eWW;
                cx=r.x;
                cy=r.y+r.height/2;
            }
            case eNE -> {
                eResizeAnchor = EAnchor.eSW;
                cx=r.x;
                cy=y+r.height;
            }
            case eNN -> {
                eResizeAnchor = EAnchor.eSS;
                cx=r.x+r.width/2;
                cy=r.y+r.height;
            }
        }
    }

    @Override
    public void drag(int x, int y) {
        double dx=0;
        double dy=0;

        switch (eResizeAnchor) {
            case eEE -> {
                dx=-(x-px);
                dy=0;
            }
            case eWW -> {
                dx=x-px;
                dy=0;
            }
            case eNN -> {
                dx=0;
                dy=y-py;
            }
            case eSS -> {
                dx=0;
                dy=-(y-py);
            }
            case eNE -> {
                dx=-(x-px);
                dy=(y-py);
            }
            case eNW -> {
                dx=x-px;
                dy=y-py;
            }
            case eSE -> {
                dx=-(x-px);
                dy=-(y-py);
            }
            case eSW -> {
                dx=x-px;
                dy=-(y-py);
            }
        }
        Shape transformedShape = this.shape.getTransformedShape();
        double w1 = this.bounds.getBounds2D().getWidth();
        double w2 = dx+w1;
        double h1 = this.bounds.getBounds2D().getHeight();
        double h2 = dy+h1;

        double xScale = w2/w1;
        double yScale = h2/h1;

        this.shape.getAffineTransform().translate(cx, cy);
        this.shape.getAffineTransform().scale(xScale, yScale);
        this.shape.getAffineTransform().translate(-cx, -cy);

        px=x;
        py=y;
    }

    @Override
    public void finish(int x, int y) {

    }

    @Override
    public void addPoint(int x, int y) {

    }
}
