package transformers;

import shapes.GShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GResize extends GTransformer {
    private int px, py;
    private GShape.EAnchor eAnchor;
    private double initialWidth, initialHeight;
    private double anchorX, anchorY;
    private AffineTransform initialTransform;

    public GResize(GShape shape) {
        super(shape);
        this.eAnchor = shape.getESelectedAnchor();

        Shape transformedShape = shape.getAffineTransform().createTransformedShape(shape.getShape());
        Rectangle2D bounds = transformedShape.getBounds2D();
        this.initialWidth = bounds.getWidth();
        this.initialHeight = bounds.getHeight();

        switch(eAnchor) {
            case eNN:
                anchorX = bounds.getCenterX();
                anchorY = bounds.getMaxY();
                break;
            case eSS:
                anchorX = bounds.getCenterX();
                anchorY = bounds.getMinY();
                break;
            case eEE:
                anchorX = bounds.getMinX();
                anchorY = bounds.getCenterY();
                break;
            case eWW:
                anchorX = bounds.getMaxX();
                anchorY = bounds.getCenterY();
                break;
            case eNE:
                anchorX = bounds.getMinX();
                anchorY = bounds.getMaxY();
                break;
            case eNW:
                anchorX = bounds.getMaxX();
                anchorY = bounds.getMaxY();
                break;
            case eSE:
                anchorX = bounds.getMinX();
                anchorY = bounds.getMinY();
                break;
            case eSW:
                anchorX = bounds.getMaxX();
                anchorY = bounds.getMinY();
                break;
        }

        this.initialTransform = new AffineTransform(shape.getAffineTransform());
    }

    @Override
    public void start(int x, int y) {
        px = x;
        py = y;
    }

    @Override
    public void drag(int x, int y) {
        int dx = x - px;
        int dy = y - py;

        double scaleX = 1.0;
        double scaleY = 1.0;
        double currentWidth = (initialWidth == 0) ? 1.0 : initialWidth;
        double currentHeight = (initialHeight == 0) ? 1.0 : initialHeight;


        switch(eAnchor) {
            case eNN:
                scaleY = (currentHeight - dy) / currentHeight;
                break;
            case eSS:
                scaleY = (currentHeight + dy) / currentHeight;
                // scaleX = 1.0;
                break;
            case eEE:
                scaleX = (currentWidth + dx) / currentWidth;
                // scaleY = 1.0;
                break;
            case eWW:
                scaleX = (currentWidth - dx) / currentWidth;
                // scaleY = 1.0;
                break;
            case eNE:
                scaleX = (currentWidth + dx) / currentWidth;
                scaleY = (currentHeight - dy) / currentHeight;
                break;
            case eNW:
                scaleX = (currentWidth - dx) / currentWidth;
                scaleY = (currentHeight - dy) / currentHeight;
                break;
            case eSE:
                scaleX = (currentWidth + dx) / currentWidth;
                scaleY = (currentHeight + dy) / currentHeight;
                break;
            case eSW:
                scaleX = (currentWidth - dx) / currentWidth;
                scaleY = (currentHeight + dy) / currentHeight;
                break;
            default:
                shape.setAffineTransform(new AffineTransform(initialTransform));
                return;
        }

        final double MIN_SCALE = 0.05;
        if (Math.abs(scaleX) < MIN_SCALE) {
            scaleX = MIN_SCALE * Math.signum(scaleX);
        }
        if (Math.abs(scaleY) < MIN_SCALE) {
            scaleY = MIN_SCALE * Math.signum(scaleY);
        }



        AffineTransform scaleOperation = new AffineTransform();
        scaleOperation.translate(anchorX, anchorY);   // 기준점으로 이동
        scaleOperation.scale(scaleX, scaleY);         // 스케일 적용
        scaleOperation.translate(-anchorX, -anchorY); // 다시 원래 위치로 이동
        AffineTransform finalTransform = new AffineTransform(scaleOperation); // scaleOperation으로 시작
        finalTransform.concatenate(initialTransform); // initialTransform을 뒤에 곱함 (pre-multiply 개념)

        shape.setAffineTransform(finalTransform);
    }

    @Override
    public void finish(int x, int y) {
    }

    @Override
    public void addPoint(int x, int y) {
    }
}