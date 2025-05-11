package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class GShape {

    public enum EPoints {
        e2P,
        eNP
    }

    private Shape shape;
    private boolean selected;
    protected ArrayList<Rectangle> anchors;
    private static final int ANCHOR_SIZE = 6;

    public GShape(Shape shape) {
        this.shape = shape;
        this.selected = false;
        this.anchors = new ArrayList<>();
    }

    protected Shape getShape() {
        return this.shape;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.draw(shape);

        if (selected) {
            drawAnchors(graphics2D);
        }
    }

    public boolean contains(int x, int y) {
        return this.shape.contains(x, y);
    }

    public boolean containedIn(Rectangle2D selectionBounds) {
        Rectangle shapeBounds = shape.getBounds();
        return selectionBounds.contains(shapeBounds) || selectionBounds.intersects(shapeBounds);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            createAnchors();
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    protected void createAnchors() {
        anchors.clear();
    }

    protected void drawAnchors(Graphics2D g2d) {
        Color originalColor = g2d.getColor();
        g2d.setColor(Color.BLACK);

        for (Rectangle anchor : anchors) {
            g2d.fill(anchor);
        }

        g2d.setColor(originalColor);
    }

    public abstract void setPoint(int x, int y);
    public abstract void addPoint(int x, int y);
    public abstract void dragPoint(int x, int y);

    public abstract void setMovePoint(int x, int y);
    public abstract void movePoint(int x, int y);
}