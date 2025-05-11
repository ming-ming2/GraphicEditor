package transformers;

import java.awt.Graphics2D;

import shapes.GShape;

public class GMover extends GTransformer {

    private GShape shape;

    public GMover(GShape shape) {
        super(shape);
        this.shape = shape;
    }
    public void start(Graphics2D graphics, int x, int y) {
        shape.setMovePoint(x, y);
    }
    public void drag(Graphics2D graphics, int x, int y) {
        shape.movePoint(x, y);
    }
    public void finish(Graphics2D graphics, int x, int y) {
    }
    @Override
    public void addPoint(Graphics2D graphics, int x, int y) {
    }
}