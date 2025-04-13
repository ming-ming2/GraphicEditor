package shapes;

import java.awt.*;

public abstract class GShape {
    protected Point start;
    protected Point end;

    public abstract void draw(Graphics2D g);

    public abstract void resize(Point end);

    public abstract boolean contains(Point p);

    public abstract Shape clone();

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public void normalize() {
        int x1 = start.x;
        int y1 = start.y;
        int x2 = end.x;
        int y2 = end.y;

        int left = Math.min(x1, x2);
        int top = Math.min(y1, y2);
        int right = Math.max(x1, x2);
        int bottom = Math.max(y1, y2);

        this.start = new Point(left, top);
        this.end = new Point(right, bottom);
    }

}
