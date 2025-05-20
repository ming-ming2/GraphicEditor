package transformers;

import shapes.GShape;

public class GRotater extends GTransformer{
    private int px, py;

    public GRotater(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {
        px = x;
        py = y;
    }

    @Override
    public void drag(int x, int y) {
        double theta = Math.atan2(py-x, py-y);
        this.shape.rotate(theta);
    }

    @Override
    public void finish(int x, int y) {

    }

    @Override
    public void addPoint(int x, int y) {

    }

}
