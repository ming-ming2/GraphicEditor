package shapes;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

public class GText extends GShape {
    private Shape textShape;
    private String text;
    private Font font;
    private int x, y;

    public GText() {
        super(createDefaultTextShape("Text"));
        this.text = "Text";
        this.font = new Font("Arial", Font.PLAIN, 20);
        this.textShape = this.getShape();
    }

    private static Shape createDefaultTextShape(String text) {
        Font defaultFont = new Font("Arial", Font.PLAIN, 20);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        GlyphVector gv = defaultFont.createGlyphVector(frc, text);
        return gv.getOutline();
    }

    @Override
    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
        updateTextShape();
    }

    @Override
    public void dragPoint(int x, int y) {
        // Not used for text
    }

    @Override
    public void addPoint(int x, int y) {
        // Not used for text
    }

    private void updateTextShape() {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        GlyphVector gv = font.createGlyphVector(frc, text);
        textShape = gv.getOutline(x, y);

        // Update the shape in the parent class
        try {
            java.lang.reflect.Field shapeField = GShape.class.getDeclaredField("shape");
            shapeField.setAccessible(true);
            shapeField.set(this, textShape);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setText(String text) {
        this.text = text;
        updateTextShape();
    }

    public void setFont(Font font) {
        this.font = font;
        updateTextShape();
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }
}