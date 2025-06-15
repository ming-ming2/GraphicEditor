package shapes;

import global.GConstants;
import global.GGraphicsProperties;

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

    private float lineWidth;
    private GGraphicsProperties.LineStyle lineStyle;
    private Color lineColor;
    private Color fillColor;

    public GShape(Shape shape) {
        this.shape = shape;
        this.affineTransform = new AffineTransform();
        this.anchors = new Ellipse2D[EAnchor.values().length - 1];
        for (int i = 0; i < this.anchors.length; i++) {
            this.anchors[i] = new Ellipse2D.Double(0, 0, ANCHOR_WIDTH, ANCHOR_HEIGHT);
        }
        this.bSelected = false;
        this.eSelectedAnchor = null;

        GGraphicsProperties props = GGraphicsProperties.getInstance();
        this.lineWidth = props.getLineWidth();
        this.lineStyle = props.getLineStyle();
        this.lineColor = props.getLineColor();
        this.fillColor = props.getFillColor();
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

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public GGraphicsProperties.LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(GGraphicsProperties.LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    protected void setAnchors() {
        Rectangle2D bounds = this.shape.getBounds2D();
        double bx = bounds.getX();
        double by = bounds.getY();
        double bw = bounds.getWidth();
        double bh = bounds.getHeight();

        Point2D.Double anchorLocalPos = new Point2D.Double();
        Point2D.Double anchorScreenPos = new Point2D.Double();

        for (int i = 0; i < this.anchors.length; i++) {
            switch (EAnchor.values()[i]) {
                case eNN: anchorLocalPos.setLocation(bx + bw / 2, by); break;
                case eNE: anchorLocalPos.setLocation(bx + bw, by); break;
                case eNW: anchorLocalPos.setLocation(bx, by); break;
                case eSS: anchorLocalPos.setLocation(bx + bw / 2, by + bh); break;
                case eSE: anchorLocalPos.setLocation(bx + bw, by + bh); break;
                case eSW: anchorLocalPos.setLocation(bx, by + bh); break;
                case eEE: anchorLocalPos.setLocation(bx + bw, by + bh / 2); break;
                case eWW: anchorLocalPos.setLocation(bx, by + bh / 2); break;
                case eRR: anchorLocalPos.setLocation(bx + bw / 2, by - 30); break;
            }

            this.affineTransform.transform(anchorLocalPos, anchorScreenPos);

            this.anchors[i].setFrame(
                    anchorScreenPos.x - (double) ANCHOR_WIDTH / 2,
                    anchorScreenPos.y - (double) ANCHOR_HEIGHT / 2,
                    ANCHOR_WIDTH,
                    ANCHOR_HEIGHT
            );
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Shape transformedShape = this.getTransformedShape();

        Stroke originalStroke = graphics2D.getStroke();
        Color originalColor = graphics2D.getColor();

        Stroke lineStroke;
        switch (lineStyle) {
            case DASHED:
                float[] dashPattern = {10.0f, 5.0f};
                lineStroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashPattern, 0);
                break;
            case DOTTED:
                float[] dotPattern = {2.0f, 3.0f};
                lineStroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dotPattern, 0);
                break;
            case SOLID:
            default:
                lineStroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                break;
        }

        graphics2D.setStroke(lineStroke);
        graphics2D.setColor(lineColor);
        graphics2D.draw(transformedShape);

        graphics2D.setStroke(originalStroke);
        graphics2D.setColor(originalColor);

        if (bSelected) {
            this.setAnchors();

            for (int i = 0; i < this.anchors.length; i++) {
                Color penColor = graphics2D.getColor();
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(this.anchors[i]);
                graphics2D.setColor(penColor);
                graphics2D.draw(this.anchors[i]);
            }
        }
    }

    public boolean contains(int x, int y) {
        if (bSelected) {
            this.setAnchors();
            for (int i = 0; i < this.anchors.length; i++) {
                if (this.anchors[i].contains(x, y)) {
                    this.eSelectedAnchor = EAnchor.values()[i];
                    return true;
                }
            }
        }

        Shape transformedShape = this.getTransformedShape();
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