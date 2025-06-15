package global;

import java.awt.*;

public class GGraphicsProperties {
    private static GGraphicsProperties instance;

    public enum LineStyle {
        SOLID, DASHED, DOTTED
    }

    private float lineWidth = GConstants.EGraphicsDefaults.eDefaultLineWidth.getFloatValue();
    private LineStyle lineStyle = LineStyle.SOLID;
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private String fontName = GConstants.EGraphicsDefaults.eDefaultFontName.getValue();
    private int fontSize = GConstants.EGraphicsDefaults.eDefaultFontSize.getIntValue();
    private int fontStyle = Font.PLAIN;

    private GGraphicsProperties() {}

    public static GGraphicsProperties getInstance() {
        if (instance == null) {
            instance = new GGraphicsProperties();
        }
        return instance;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
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

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public Font createFont() {
        return new Font(fontName, fontStyle, fontSize);
    }

    public Stroke createStroke() {
        switch (lineStyle) {
            case DASHED:
                float[] dashPattern = GConstants.EGraphicsDefaults.eDashPattern.getFloatArray();
                return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashPattern, 0);
            case DOTTED:
                float[] dotPattern = GConstants.EGraphicsDefaults.eDotPattern.getFloatArray();
                return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dotPattern, 0);
            case SOLID:
            default:
                return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }
    }
}