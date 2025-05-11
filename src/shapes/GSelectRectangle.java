package shapes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public class GSelectRectangle extends GRectangle {
    private Rectangle2D rectangle;

    public GSelectRectangle() {
        super();
        this.rectangle = (Rectangle2D) this.getShape();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        Stroke originalStroke = graphics2D.getStroke();
        float[] dash = {5.0f, 5.0f};
        BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);

        graphics2D.setStroke(dashedStroke);
        graphics2D.draw(this.rectangle);

        graphics2D.setStroke(originalStroke);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(
                rectangle.getX(),
                rectangle.getY(),
                rectangle.getWidth(),
                rectangle.getHeight()
        );
    }
}