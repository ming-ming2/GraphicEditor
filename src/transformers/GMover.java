package transformers;

import shapes.GShape;

import java.awt.*;

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
        int dx = x-px;
        int dy = y-py;
        this.shape.getAffineTransform().translate(dx, dy);
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
