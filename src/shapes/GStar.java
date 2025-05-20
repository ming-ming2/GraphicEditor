package shapes;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.lang.reflect.Field;

public class GStar extends GShape {
    private GeneralPath path;
    private int numPoints;
    private double innerRadius;
    private double outerRadius;
    private int centerX;
    private int centerY;

    public GStar() {
        this(0, 0, 5, 25.0, 50.0);
    }

    public GStar(int centerX, int centerY, int numPoints, double innerRadius, double outerRadius) {
        super(createStarShape(centerX, centerY, numPoints, innerRadius, outerRadius));
        this.centerX = centerX;
        this.centerY = centerY;
        this.numPoints = numPoints;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.path = (GeneralPath) this.getShape();
    }

    private static GeneralPath createStarShape(int centerX, int centerY, int numPoints, double innerRadius, double outerRadius) {
        GeneralPath path = new GeneralPath();
        if (numPoints < 2 || outerRadius <= 0 || innerRadius <= 0 || innerRadius >= outerRadius) {
            path.moveTo(centerX, centerY);
            path.lineTo(centerX, centerY);
            path.closePath();
            return path;
        }

        double angleStep = Math.PI / numPoints;
        double currentAngle = -Math.PI / 2.0;

        for (int i = 0; i < numPoints * 2; i++) {
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            double pointX = centerX + radius * Math.cos(currentAngle);
            double pointY = centerY + radius * Math.sin(currentAngle);

            if (i == 0) {
                path.moveTo(pointX, pointY);
            } else {
                path.lineTo(pointX, pointY);
            }
            currentAngle += angleStep;
        }

        path.closePath();
        return path;
    }

    private void updateShapeInParent(Shape newShape) {
        try {
            Field shapeField = GShape.class.getDeclaredField("shape");
            shapeField.setAccessible(true);
            shapeField.set(this, newShape);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void regeneratePathAndUpdate() {
        this.path = createStarShape(this.centerX, this.centerY, this.numPoints, this.innerRadius, this.outerRadius);
        updateShapeInParent(this.path);
    }


    @Override
    public void setPoint(int x, int y) {
        this.centerX = x;
        this.centerY = y;
        regeneratePathAndUpdate();
    }


    @Override
    public void dragPoint(int x, int y) {
        double dx = x - this.centerX;
        double dy = y - this.centerY;
        double newOuterRadius = Math.sqrt(dx * dx + dy * dy);

        if (newOuterRadius < 2.0) newOuterRadius = 2.0;
        double newInnerRadius = newOuterRadius * 0.5;
        if (newInnerRadius < 1.0) newInnerRadius = 1.0;

        if (newInnerRadius >= newOuterRadius) {
            newInnerRadius = newOuterRadius * 0.9;
            if (newInnerRadius < 1.0) newInnerRadius = 1.0;
        }

        this.outerRadius = newOuterRadius;
        this.innerRadius = newInnerRadius;

        regeneratePathAndUpdate();
    }

    @Override
    public void addPoint(int x, int y) {
    }

    public void setNumPoints(int numPoints) {
        if (numPoints >= 2 && this.numPoints != numPoints) {
            this.numPoints = numPoints;
            regeneratePathAndUpdate();
        }
    }

    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }
    public int getNumPoints() { return numPoints; }
    public double getInnerRadius() { return innerRadius; }
    public double getOuterRadius() { return outerRadius; }

    public void setInnerRadius(double innerRadius) {
        if (innerRadius > 0 && innerRadius < this.outerRadius && this.innerRadius != innerRadius) {
            this.innerRadius = innerRadius;
            regeneratePathAndUpdate();
        }
    }

    public void setOuterRadius(double outerRadius) {
        if (outerRadius > 0 && outerRadius > this.innerRadius && this.outerRadius != outerRadius) {
            this.outerRadius = outerRadius;
            regeneratePathAndUpdate();
        }
    }
}