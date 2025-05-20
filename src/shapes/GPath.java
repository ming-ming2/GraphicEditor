package shapes;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GPath extends GShape {
    private Path2D path;
    private ArrayList<Point2D> points;

    public GPath() {
        super(new Path2D.Float());
        this.path = (Path2D) this.getShape();
        this.points = new ArrayList<>();
    }

    @Override
    public void setPoint(int x, int y) {
        points.clear();
        points.add(new Point2D.Float(x, y));
        path.reset();
        path.moveTo(x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        if (!points.isEmpty()) {
            points.add(new Point2D.Float(x, y));
            path.lineTo(x, y);
        }
    }

    @Override
    public void addPoint(int x, int y) {
        if (!points.isEmpty()) {
            points.add(new Point2D.Float(x, y));
            path.lineTo(x, y);
        }
    }

    public void closePath() {
        path.closePath();
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }
}