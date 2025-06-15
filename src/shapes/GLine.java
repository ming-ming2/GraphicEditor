package shapes;

import java.awt.geom.Line2D;
import java.awt.Shape;
import java.awt.BasicStroke;
import global.GConstants;
import global.GConstants.EAnchor;

public class GLine extends GShape {
    private Line2D line;
    private static final float CLICK_TOLERANCE = GConstants.EShapeConstants.eClickTolerance.getFloatValue();

    public GLine() {
        super(new Line2D.Float(0, 0, 0, 0));
        this.line = (Line2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.line.setLine(x, y, x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        double x1 = line.getX1();
        double y1 = line.getY1();
        this.line.setLine(x1, y1, x, y);
    }

    @Override
    public void addPoint(int x, int y) {
    }

    @Override
    public boolean contains(int x, int y) {
        if (isSelected()) {
            this.setAnchors();
            for (int i = 0; i < this.getAnchors().length; i++) {
                if (this.getAnchors()[i].contains(x, y)) {
                    this.eSelectedAnchor = EAnchor.values()[i];
                    return true;
                }
            }
        }

        Shape transformedShape = this.getTransformedShape();
        if (transformedShape.getBounds().contains(x, y)) {
            this.eSelectedAnchor = EAnchor.eMM;
            return true;
        }

        return false;
    }
}