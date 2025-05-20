package transformers;

import shapes.GShape;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GMover extends GTransformer {
    int px, py;

    public GMover(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {
        px = x;
        py = y;
    }

    @Override
    public void drag(int x, int y) {
        int dx = x - px;
        int dy = y - py;

        AffineTransform translateTransform = new AffineTransform();
        translateTransform.translate(dx, dy);

        AffineTransform currentTransform = this.shape.getAffineTransform();
        AffineTransform newTransform = new AffineTransform();

        double m00 = currentTransform.getScaleX();
        double m01 = currentTransform.getShearX();
        double m02 = currentTransform.getTranslateX() + dx;
        double m10 = currentTransform.getShearY();
        double m11 = currentTransform.getScaleY();
        double m12 = currentTransform.getTranslateY() + dy;

        newTransform.setTransform(m00, m10, m01, m11, m02, m12);
        this.shape.getAffineTransform().setTransform(newTransform);

        px = x;
        py = y;
    }

    @Override
    public void finish(int x, int y) {
    }

    @Override
    public void addPoint(int x, int y) {
    }
}