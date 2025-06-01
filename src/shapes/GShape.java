package shapes;

import global.GConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.NoninvertibleTransformException;
import java.io.Serializable;


import static global.GConstants.*;

public abstract class GShape implements Serializable {
    private final static int ANCHOR_WIDTH = 10;
    private final static int ANCHOR_HEIGHT = 10;
    private final AffineTransform affineTransform;
    private Shape shape;
    private Ellipse2D[] anchors;
    private boolean bSelected;
    private EAnchor eSelectedAnchor;

    public GShape(Shape shape) {
        this.shape = shape;
        this.affineTransform = new AffineTransform();
        this.anchors = new Ellipse2D[EAnchor.values().length - 1];
        for (int i = 0; i < this.anchors.length; i++) {
            this.anchors[i] = new Ellipse2D.Double(0, 0, ANCHOR_WIDTH, ANCHOR_HEIGHT);
        }
        this.bSelected = false;
        this.eSelectedAnchor = null;
    }

    public Shape getTransformedShape() {
        return this.affineTransform.createTransformedShape(this.shape);
    }

    public boolean contains(GShape otherShape) {
        return this.getTransformedShape().getBounds2D().intersects(otherShape.getTransformedShape().getBounds2D());
    }

    protected Shape[] getAnchors() {
        return this.anchors;
    }

    public enum EPoints {
        e2P,
        eNP;
    }

    public Shape getShape() {
        return this.shape;
    }

    public boolean isSelected() {
        return this.bSelected;
    }

    public void setSelected(boolean bSelected) {
        this.bSelected = bSelected;
    }

    public EAnchor getESelectedAnchor() {
        return this.eSelectedAnchor;
    }

    public Rectangle2D getBounds() {
        return shape.getBounds2D();
    }

    protected void setAnchors() {
        Rectangle2D bounds = this.shape.getBounds2D();
        double bx = bounds.getX();
        double by = bounds.getY();
        double bw = bounds.getWidth();
        double bh = bounds.getHeight();

        Point2D.Double anchorCenterPos = new Point2D.Double();

        for (int i = 0; i < this.anchors.length; i++) {
            switch (EAnchor.values()[i]) {
                case eNN: anchorCenterPos.setLocation(bx + bw / 2, by); break;
                case eNE: anchorCenterPos.setLocation(bx + bw, by); break;
                case eNW: anchorCenterPos.setLocation(bx, by); break;
                case eSS: anchorCenterPos.setLocation(bx + bw / 2, by + bh); break;
                case eSE: anchorCenterPos.setLocation(bx + bw, by + bh); break;
                case eSW: anchorCenterPos.setLocation(bx, by + bh); break;
                case eEE: anchorCenterPos.setLocation(bx + bw, by + bh / 2); break;
                case eWW: anchorCenterPos.setLocation(bx, by + bh / 2); break;
                case eRR: anchorCenterPos.setLocation(bx + bw / 2, by - 30); break;
            }
            this.anchors[i].setFrame(
                    anchorCenterPos.x - (double) ANCHOR_WIDTH / 2,
                    anchorCenterPos.y - (double) ANCHOR_HEIGHT / 2,
                    ANCHOR_WIDTH,
                    ANCHOR_HEIGHT
            );
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        graphics2D.draw(transformedShape);

        if (bSelected) {
            this.setAnchors();

            for (int i = 0; i < this.anchors.length; i++) {
                AffineTransform anchorTransform = new AffineTransform();
                try {
                    Point2D.Double anchorLocalCenter = new Point2D.Double(this.anchors[i].getCenterX(), this.anchors[i].getCenterY());
                    Point2D.Double anchorScreenCenter = new Point2D.Double();
                    this.affineTransform.transform(anchorLocalCenter, anchorScreenCenter);

                    anchorTransform.setToTranslation(anchorScreenCenter.getX(), anchorScreenCenter.getY());

                    Shape fixedSizeAnchorShape = new Ellipse2D.Double(
                            -(double)ANCHOR_WIDTH / 2,
                            -(double)ANCHOR_HEIGHT / 2,
                            ANCHOR_WIDTH,
                            ANCHOR_HEIGHT
                    );
                    Shape finalAnchorShape = anchorTransform.createTransformedShape(fixedSizeAnchorShape);

                    Color penColor = graphics2D.getColor();
                    graphics2D.setColor(graphics2D.getBackground());
                    graphics2D.fill(finalAnchorShape);
                    graphics2D.setColor(penColor);
                    graphics2D.draw(finalAnchorShape);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public boolean contains(int x, int y) {
        if (bSelected) {
            this.setAnchors();
            for (int i = 0; i < this.anchors.length; i++) {
                Point2D.Double anchorLocalCenter = new Point2D.Double(this.anchors[i].getCenterX(), this.anchors[i].getCenterY());
                Point2D.Double anchorScreenCenter = new Point2D.Double();
                this.affineTransform.transform(anchorLocalCenter, anchorScreenCenter);

                Rectangle2D.Double anchorBoundsOnScreen = new Rectangle2D.Double(
                        anchorScreenCenter.getX() - (double) ANCHOR_WIDTH / 2,
                        anchorScreenCenter.getY() - (double) ANCHOR_HEIGHT / 2,
                        ANCHOR_WIDTH,
                        ANCHOR_HEIGHT
                );

                if (anchorBoundsOnScreen.contains(x, y)) {
                    this.eSelectedAnchor = EAnchor.values()[i];
                    return true;
                }
            }
        }

        Shape transformedShape = this.affineTransform.createTransformedShape(shape);
        if (transformedShape.contains(x, y)) {
            this.eSelectedAnchor = EAnchor.eMM;
            return true;
        }
        return false;
    }

    public AffineTransform getAffineTransform() {
        return this.affineTransform;
    }


    public abstract void addPoint(int x, int y);
    public abstract void setPoint(int x, int y);
    public abstract void dragPoint(int x, int y);
}