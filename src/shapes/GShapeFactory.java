package shapes;

import types.GShapeType;

public class GShapeFactory {
    public static GShape createShape(GShapeType type) {
        switch (type) {
            case RECTANGLE:
                return new GRectangle();
            case POLYGON:
                return new GPolygon();
            default:
                return null;
        }
    }
}
