package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GGroup extends GShape {
    private List<GShape> children;
    private Rectangle2D groupBounds;

    public GGroup() {
        super(new Rectangle2D.Float(0, 0, 0, 0));
        this.children = new ArrayList<>();
        this.groupBounds = (Rectangle2D) this.getShape();
        updateGroupBounds();
    }

    public GGroup(List<GShape> shapes) {
        super(new Rectangle2D.Float(0, 0, 0, 0));
        this.children = new ArrayList<>(shapes);
        this.groupBounds = (Rectangle2D) this.getShape();
        updateGroupBounds();
    }

    public void addShape(GShape shape) {
        children.add(shape);
        updateGroupBounds();
    }

    public void removeShape(GShape shape) {
        children.remove(shape);
        updateGroupBounds();
    }

    public List<GShape> getChildren() {
        return new ArrayList<>(children);
    }

    public void ungroup() {
        for (GShape child : children) {
            child.getAffineTransform().preConcatenate(this.getAffineTransform());
        }
    }

    private void updateGroupBounds() {
        if (children.isEmpty()) {
            groupBounds.setFrame(0, 0, 0, 0);
            return;
        }

        Rectangle2D bounds = null;
        for (GShape child : children) {
            Rectangle2D childBounds = child.getTransformedShape().getBounds2D();
            if (bounds == null) {
                bounds = new Rectangle2D.Double(childBounds.getX(), childBounds.getY(),
                        childBounds.getWidth(), childBounds.getHeight());
            } else {
                bounds = bounds.createUnion(childBounds);
            }
        }

        groupBounds.setFrame(bounds);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        AffineTransform originalTransform = graphics2D.getTransform();

        graphics2D.transform(this.getAffineTransform());

        for (GShape child : children) {
            child.draw(graphics2D);
        }

        graphics2D.setTransform(originalTransform);

        if (isSelected()) {
            this.setAnchors();

            for (int i = 0; i < this.getAnchors().length; i++) {
                Color penColor = graphics2D.getColor();
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(this.getAnchors()[i]);
                graphics2D.setColor(penColor);
                graphics2D.draw(this.getAnchors()[i]);
            }
        }
    }

    @Override
    public Shape getTransformedShape() {
        updateGroupBounds();
        return this.getAffineTransform().createTransformedShape(groupBounds);
    }

    @Override
    public boolean contains(int x, int y) {
        updateGroupBounds();
        return super.contains(x, y);
    }

    @Override
    public void setPoint(int x, int y) {
        // 그룹은 직접 그리기로 생성되지 않음
    }

    @Override
    public void dragPoint(int x, int y) {
        // 그룹은 직접 그리기로 생성되지 않음
    }

    @Override
    public void addPoint(int x, int y) {
        // 그룹은 직접 그리기로 생성되지 않음
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public int size() {
        return children.size();
    }
}