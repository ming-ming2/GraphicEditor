package transformers;

import shapes.GShape;

import java.awt.*;

public class GResize extends GTransformer {
    int px, py;

    public GResize(GShape shape) {
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

        double scaleX = this.shape.getWidth()+dx/this.shape.getWidth();
        double scaleY = this.shape.getHeight()+dy/this.shape.getHeight();
        this.shape.scale(scaleX, scaleY);

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
