package transformers;

import global.GConstants.EAnchor;
import shapes.GShape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Rectangle;

public class GResizer extends GTransformer {

    private EAnchor anchor;
    private Rectangle originalBounds;
    private AffineTransform originalTransform;

    private double rotationAngle;

    private Point2D.Double anchorPoint;
    private Point2D.Double oppositePoint;

    private Point2D.Double transformedOpposite;

    private boolean initialFlipX = false;
    private boolean initialFlipY = false;
    private double initialScaleXMagnitude = 1.0;
    private double initialScaleYMagnitude = 1.0;

    private static final double MIN_SIZE = 5.0;
    private static final double EPSILON = 1e-6;

    public GResizer(GShape shape) {
        super(shape);
        this.anchorPoint = new Point2D.Double();
        this.oppositePoint = new Point2D.Double();
        this.transformedOpposite = new Point2D.Double();
    }

    @Override
    public void start(int x, int y) {
        this.anchor = shape.getESelectedAnchor();
        if (anchor == null || anchor == EAnchor.eMM || anchor == EAnchor.eRR) {
            return;
        }

        this.originalBounds = shape.getShape().getBounds();
        this.originalTransform = new AffineTransform(shape.getAffineTransform());

        double[] matrix = new double[6];
        originalTransform.getMatrix(matrix);

        this.rotationAngle = Math.atan2(matrix[1], matrix[0]);

        this.initialScaleXMagnitude = Math.sqrt(matrix[0] * matrix[0] + matrix[1] * matrix[1]);
        this.initialScaleYMagnitude = Math.sqrt(matrix[2] * matrix[2] + matrix[3] * matrix[3]);

        this.initialFlipX = (matrix[0] * Math.cos(rotationAngle) + matrix[1] * Math.sin(rotationAngle)) < 0;
        this.initialFlipY = (matrix[2] * -Math.sin(rotationAngle) + matrix[3] * Math.cos(rotationAngle)) < 0;

        calculateAnchorPoints();
        originalTransform.transform(oppositePoint, transformedOpposite);
    }

    private void calculateAnchorPoints() {
        double minX = originalBounds.getMinX();
        double minY = originalBounds.getMinY();
        double maxX = originalBounds.getMaxX();
        double maxY = originalBounds.getMaxY();
        double midX = originalBounds.getCenterX();
        double midY = originalBounds.getCenterY();

        switch (anchor) {
            case eNW: anchorPoint.setLocation(minX, minY); oppositePoint.setLocation(maxX, maxY); break;
            case eNN: anchorPoint.setLocation(midX, minY); oppositePoint.setLocation(midX, maxY); break;
            case eNE: anchorPoint.setLocation(maxX, minY); oppositePoint.setLocation(minX, maxY); break;
            case eWW: anchorPoint.setLocation(minX, midY); oppositePoint.setLocation(maxX, midY); break;
            case eEE: anchorPoint.setLocation(maxX, midY); oppositePoint.setLocation(minX, midY); break;
            case eSW: anchorPoint.setLocation(minX, maxY); oppositePoint.setLocation(maxX, minY); break;
            case eSS: anchorPoint.setLocation(midX, maxY); oppositePoint.setLocation(midX, minY); break;
            case eSE: anchorPoint.setLocation(maxX, maxY); oppositePoint.setLocation(minX, minY); break;
            default: break;
        }
    }

    @Override
    public void drag(int x, int y) {
        if (anchor == null || anchor == EAnchor.eMM || anchor == EAnchor.eRR) {
            return;
        }

        Point2D.Double mouseScreen = new Point2D.Double(x, y);
        Point2D.Double mouseLocal;

        try {
            mouseLocal = (Point2D.Double) originalTransform.inverseTransform(mouseScreen, null);
        } catch (Exception e) {
            return;
        }

        double origVectorX = anchorPoint.x - oppositePoint.x;
        double origVectorY = anchorPoint.y - oppositePoint.y;
        double currentVectorX = mouseLocal.x - oppositePoint.x;
        double currentVectorY = mouseLocal.y - oppositePoint.y;

        double finalTotalScaleX;
        double finalTotalScaleY;

        double targetMagnitudeX = initialScaleXMagnitude;
        double targetMagnitudeY = initialScaleYMagnitude;

        if (anchor == EAnchor.eWW || anchor == EAnchor.eEE ||
                anchor == EAnchor.eNW || anchor == EAnchor.eNE ||
                anchor == EAnchor.eSW || anchor == EAnchor.eSE) {
            if (Math.abs(origVectorX) > EPSILON) {
                targetMagnitudeX = Math.abs(currentVectorX / origVectorX) * initialScaleXMagnitude;
            } else {
                targetMagnitudeX = initialScaleXMagnitude;
            }
        }

        if (anchor == EAnchor.eNN || anchor == EAnchor.eSS ||
                anchor == EAnchor.eNW || anchor == EAnchor.eNE ||
                anchor == EAnchor.eSW || anchor == EAnchor.eSE) {
            if (Math.abs(origVectorY) > EPSILON) {
                targetMagnitudeY = Math.abs(currentVectorY / origVectorY) * initialScaleYMagnitude;
            } else {
                targetMagnitudeY = initialScaleYMagnitude;
            }
        }

        boolean currentFlipX = this.initialFlipX;
        if (anchor == EAnchor.eWW || anchor == EAnchor.eEE ||
                anchor == EAnchor.eNW || anchor == EAnchor.eNE ||
                anchor == EAnchor.eSW || anchor == EAnchor.eSE) {
            if (origVectorX * currentVectorX < 0 && Math.abs(origVectorX) > EPSILON) {
                currentFlipX = !this.initialFlipX;
            }
        }
        finalTotalScaleX = currentFlipX ? -targetMagnitudeX : targetMagnitudeX;

        boolean currentFlipY = this.initialFlipY;
        if (anchor == EAnchor.eNN || anchor == EAnchor.eSS ||
                anchor == EAnchor.eNW || anchor == EAnchor.eNE ||
                anchor == EAnchor.eSW || anchor == EAnchor.eSE) {
            if (origVectorY * currentVectorY < 0 && Math.abs(origVectorY) > EPSILON) {
                currentFlipY = !this.initialFlipY;
            }
        }
        finalTotalScaleY = currentFlipY ? -targetMagnitudeY : targetMagnitudeY;

        if (originalBounds.getWidth() > EPSILON && Math.abs(finalTotalScaleX * originalBounds.getWidth()) < MIN_SIZE && Math.abs(finalTotalScaleX) > EPSILON) {
            finalTotalScaleX = (MIN_SIZE / originalBounds.getWidth()) * Math.signum(finalTotalScaleX);
        }
        if (originalBounds.getHeight() > EPSILON && Math.abs(finalTotalScaleY * originalBounds.getHeight()) < MIN_SIZE && Math.abs(finalTotalScaleY) > EPSILON) {
            finalTotalScaleY = (MIN_SIZE / originalBounds.getHeight()) * Math.signum(finalTotalScaleY);
        }

        if (originalBounds.getWidth() < EPSILON && Math.abs(finalTotalScaleX) > 1000) finalTotalScaleX = Math.signum(finalTotalScaleX) * 1000;
        if (originalBounds.getHeight() < EPSILON && Math.abs(finalTotalScaleY) > 1000) finalTotalScaleY = Math.signum(finalTotalScaleY) * 1000;

        AffineTransform at = new AffineTransform();
        at.translate(transformedOpposite.x, transformedOpposite.y);
        at.rotate(this.rotationAngle);
        at.scale(finalTotalScaleX, finalTotalScaleY);
        at.translate(-oppositePoint.x, -oppositePoint.y);

        shape.getAffineTransform().setTransform(at);
    }

    @Override
    public void finish(int x, int y) {
        this.anchor = null;
    }

    @Override
    public void addPoint(int x, int y) {
    }
}