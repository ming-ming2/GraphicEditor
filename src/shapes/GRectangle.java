package shapes;

import java.awt.*;

public class GRectangle extends GShape{
    @Override
    public void draw(Graphics2D g) {
        normalize();
        g.drawRect(start.x, start.y, end.x - start.x, end.y - start.y);
    }

    @Override
    public void resize(Point end) {

    }

    @Override
    public boolean contains(Point p) {
        return false;
    }

    @Override
    public Shape clone() {
        return null;
    }
}
