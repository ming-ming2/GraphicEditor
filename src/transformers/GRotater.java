package transformers;

import shapes.GShape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Rectangle;

public class GRotater extends GTransformer {
    private double cx, cy;
    private double startAngle;
    private AffineTransform originalTransform;

    public GRotater(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {
        Rectangle bounds = shape.getTransformedShape().getBounds();
        cx = bounds.getCenterX();
        cy = bounds.getCenterY();
        startAngle = Math.atan2(y - cy, x - cx);

        originalTransform = new AffineTransform(shape.getAffineTransform());
    }

    @Override
    public void drag(int x, int y) {
        double currentAngle = Math.atan2(y - cy, x - cx);
        double deltaAngle = currentAngle - startAngle;

        AffineTransform newTransform = new AffineTransform(originalTransform);

        AffineTransform rotateTransform = new AffineTransform();
        rotateTransform.translate(cx, cy);
        rotateTransform.rotate(deltaAngle);
        rotateTransform.translate(-cx, -cy);

        newTransform.preConcatenate(rotateTransform);

        this.shape.getAffineTransform().setTransform(newTransform);

        originalTransform = new AffineTransform(newTransform);
        startAngle = currentAngle;
    }

    @Override
    public void finish(int x, int y) {
    }

    @Override
    public void addPoint(int x, int y) {
    }
}