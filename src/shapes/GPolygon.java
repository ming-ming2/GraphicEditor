package shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GPolygon extends GShape {
    private List<Point> points;
    private Point lastPoint; // 미리보기용 마지막 점

    public GPolygon() {
        this.points = new ArrayList<>();
        this.lastPoint = null;
    }

    public void addPoint(Point p) {
        points.add(new Point(p));
    }

    public void setLastPoint(Point p) {
        this.lastPoint = new Point(p);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.0f));


        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }


        if (points.size() > 2 && lastPoint == null) {
            Point first = points.get(0);
            Point last = points.get(points.size() - 1);
            g.drawLine(last.x, last.y, first.x, first.y);
        }


        if (lastPoint != null && !points.isEmpty()) {
            Point lastFixed = points.get(points.size() - 1);
            g.drawLine(lastFixed.x, lastFixed.y, lastPoint.x, lastPoint.y);
        }
    }

    @Override
    public void resize(Point end) {
        setLastPoint(end);
    }

    @Override
    public boolean contains(Point p) {
        if (points.isEmpty()) return false;
        int minX = points.stream().mapToInt(pt -> pt.x).min().orElse(0);
        int maxX = points.stream().mapToInt(pt -> pt.x).max().orElse(0);
        int minY = points.stream().mapToInt(pt -> pt.y).min().orElse(0);
        int maxY = points.stream().mapToInt(pt -> pt.y).max().orElse(0);
        return p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY;
    }

    @Override
    public GShape clone() {
        GPolygon copy = new GPolygon();
        for (Point p : points) {
            copy.addPoint(new Point(p));
        }
        if (lastPoint != null) {
            copy.setLastPoint(new Point(lastPoint));
        }
        return copy;
    }

    public void complete() {
        lastPoint = null; // 다각형 완성 시 미리보기 점 제거
    }

    public List<Point> getPoints() {
        return points;
    }
}