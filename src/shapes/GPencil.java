package shapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class GPencil extends GShape {
    private GeneralPath path;
    private ArrayList<Integer> xPoints;
    private ArrayList<Integer> yPoints;
    private float strokeWidth = 1.0f;

    public GPencil() {
        super(new GeneralPath());
        this.path = (GeneralPath) this.getShape();
        this.xPoints = new ArrayList<>();
        this.yPoints = new ArrayList<>();
    }

    @Override
    public void setPoint(int x, int y) {
        xPoints.clear();
        yPoints.clear();
        xPoints.add(x);
        yPoints.add(y);
        path.reset();
        path.moveTo(x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        xPoints.add(x);
        yPoints.add(y);
        path.lineTo(x, y);
    }

    @Override
    public void addPoint(int x, int y) {
        xPoints.add(x);
        yPoints.add(y);
        path.lineTo(x, y);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        BasicStroke originalStroke = (BasicStroke) graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Shape transformedShape = this.getAffineTransform().createTransformedShape(path);
        graphics2D.draw(transformedShape);

        if(isSelected()){
            this.setAnchors();
            for(int i = 0; i < this.getAnchors().length; i++){
                Shape transformedAnchor = this.getAffineTransform().createTransformedShape(this.getAnchors()[i]);
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(transformedAnchor);
                graphics2D.draw(transformedAnchor);
            }
        }

        graphics2D.setStroke(originalStroke);
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }
}