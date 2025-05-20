package global;

import shapes.*;

import java.lang.reflect.InvocationTargetException;

public class GConstants {
    public final static class GMainFrame {
        public final static int WIDTH = 1200;
        public final static int HEIGHT = 600;
    }

    public static enum EAnchor {
        eNN(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR)),
        eNE(new java.awt.Cursor(java.awt.Cursor.NE_RESIZE_CURSOR)),
        eNW(new java.awt.Cursor(java.awt.Cursor.NW_RESIZE_CURSOR)),
        eSS(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR)),
        eSE(new java.awt.Cursor(java.awt.Cursor.SE_RESIZE_CURSOR)),
        eSW(new java.awt.Cursor(java.awt.Cursor.SW_RESIZE_CURSOR)),
        eEE(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR)),
        eWW(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR)),
        eRR(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)),
        eMM(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        private java.awt.Cursor cursor;

        EAnchor(java.awt.Cursor cursor) {
            this.cursor = cursor;
        }

        public java.awt.Cursor getCursor() {
            return this.cursor;
        }
    }

    public static enum EShapeTool {
        eSelect("select", GShape.EPoints.e2P, GRectangle.class),
        eRectangle("rectangle", GShape.EPoints.e2P, GRectangle.class),
        eEllipse("ellipse", GShape.EPoints.e2P, GEllipse.class),
        eLine("line", GShape.EPoints.e2P, GLine.class),
        ePolygon("polygon", GShape.EPoints.eNP, GPolygon.class),
        eRoundRectangle("round rectangle", GShape.EPoints.e2P, GRoundRectangle.class),
        eArc("arc", GShape.EPoints.e2P, GArc.class),
        eQuadCurve("quad curve", GShape.EPoints.eNP, GQuadCurve.class),
        eCubicCurve("cubic curve", GShape.EPoints.eNP, GCubicCurve.class),
        ePath("path", GShape.EPoints.eNP, GPath.class),
        ePencil("pencil", GShape.EPoints.eNP, GPencil.class),
        eText("text", GShape.EPoints.e2P, GText.class),
        eStar("star", GShape.EPoints.e2P, GStar.class);

        private String name;
        private GShape.EPoints ePoints;
        private Class<?> classShape;
        private EShapeTool(String name, GShape.EPoints ePoints, Class<?> classShape) {
            this.name = name;
            this.ePoints = ePoints;
            this.classShape = classShape;
        }
        public String getName() {
            return this.name;
        }
        public GShape.EPoints getEPoints() {
            return this.ePoints;
        }
        public GShape newShape() {
            try {
                GShape shape = (GShape) classShape.getConstructor().newInstance();
                return shape;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}