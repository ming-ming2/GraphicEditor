package shapes;

import java.awt.*;

public class GRectangle extends GShape {
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);

        // 시작점과 끝점을 정규화하여 그리기
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);

        g.drawRect(x, y, width, height);
    }

    @Override
    public void resize(Point end) {
        this.end = new Point(end);
    }

    @Override
    public boolean contains(Point p) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);

        return p.x >= x && p.x <= x + width &&
                p.y >= y && p.y <= y + height;
    }

    @Override
    public GShape clone() {
        GShape copy = new GRectangle();
        if (start != null) copy.setStart(new Point(start));
        if (end != null) copy.setEnd(new Point(end));
        return copy;
    }
}