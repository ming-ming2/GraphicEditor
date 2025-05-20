package shapes;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Shape;

public class GImage extends GShape {
    private Rectangle2D rectangle;
    private BufferedImage image;

    public GImage(BufferedImage image) {
        super(new Rectangle2D.Float(0, 0, image.getWidth(), image.getHeight()));
        this.rectangle = (Rectangle2D) this.getShape();
        this.image = image;
    }

    @Override
    public void setPoint(int x, int y) {
        this.rectangle.setFrame(x, y, image.getWidth(), image.getHeight());
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = rectangle.getX();
        double oy = rectangle.getY();
        double w = x - ox;
        double h = y - oy;
        this.rectangle.setFrame(ox, oy, w, h);
    }

    @Override
    public void addPoint(int x, int y) {
        // Not needed for image
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        // Calculate the bounds of the transformed shape
        Shape transformedShape = this.getAffineTransform().createTransformedShape(rectangle);
        Rectangle2D bounds = transformedShape.getBounds2D();

        // Draw the image using the transformation
        graphics2D.drawImage(image,
                (int)bounds.getX(), (int)bounds.getY(),
                (int)bounds.getWidth(), (int)bounds.getHeight(),
                null);

        // If selected, draw the anchors
        if(isSelected()){
            this.setAnchors();
            for(int i = 0; i < this.getAnchors().length; i++){
                Shape transformedAnchor = this.getAffineTransform().createTransformedShape(this.getAnchors()[i]);
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(transformedAnchor);
                graphics2D.setColor(graphics2D.getColor());
                graphics2D.draw(transformedAnchor);
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.rectangle.setFrame(rectangle.getX(), rectangle.getY(),
                image.getWidth(), image.getHeight());
    }
}