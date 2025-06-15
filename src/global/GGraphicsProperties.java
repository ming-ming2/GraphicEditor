package global;

import java.awt.*;

public class GGraphicsProperties {
    private static GGraphicsProperties instance;

    public enum LineStyle {
        SOLID, DASHED, DOTTED
    }

    private float lineWidth = 2.0f;
    private LineStyle lineStyle = LineStyle.SOLID;
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;

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

    public Stroke createStroke() {
        switch (lineStyle) {
            case DASHED:
                float[] dashPattern = {10.0f, 5.0f};
                return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashPattern, 0);
            case DOTTED:
                float[] dotPattern = {2.0f, 3.0f};
                return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dotPattern, 0);
            case SOLID:
            default:
                return new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }
    }
}