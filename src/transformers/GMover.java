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

        AffineTransform currentTransform = this.shape.getAffineTransform();

        double m00 = currentTransform.getScaleX();
        double m01 = currentTransform.getShearX();
        double m02 = currentTransform.getTranslateX() + dx;
        double m10 = currentTransform.getShearY();
        double m11 = currentTransform.getScaleY();
        double m12 = currentTransform.getTranslateY() + dy;

        currentTransform.setTransform(m00, m10, m01, m11, m02, m12);

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