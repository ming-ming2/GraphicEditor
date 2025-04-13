package shapes;

import java.awt.*;

public abstract class GShape {
    protected Point start;
    protected Point end;

    public abstract void draw(Graphics2D g);

    public abstract void resize(Point end);

    public abstract boolean contains(Point p);

    public abstract GShape clone();

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}