package global;

import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GShape;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class GConstants {
    public final static class GMainFrame {
        public final static int WIDTH = 1200;
        public final static int HEIGHT = 600;
    }

    public static enum EAnchor {
        eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNE(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNW(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSE(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSW(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
        eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
        eRR(new Cursor(Cursor.HAND_CURSOR)),
        eMM(new Cursor(Cursor.MOVE_CURSOR));
        private Cursor cursor;

        EAnchor(Cursor cursor) {
            this.cursor = cursor;
        }

        public Cursor getCursor() {
            return this.cursor;
        }

    }

    public static enum EShapeTool {
        eSelect("select", GShape.EPoints.e2P, GRectangle.class),
        eRectangle("rectangle", GShape.EPoints.e2P, GRectangle.class),
        eEllipse("ellipse", GShape.EPoints.e2P, GRectangle.class),
        eLine("line", GShape.EPoints.e2P, GRectangle.class),
        ePolygon("polygon", GShape.EPoints.eNP, GPolygon.class);

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

    public enum EFileMenuItem {
        eNew("새 파일", "newPanel"),
        eOpen("열기", "open"),
        eSave("저장", "save"),
        eSaveAs("다른 이름으로 저장", "saveAs"),
        ePrint("프린트", "print"),
        eClose("닫기", "close"),
        eQuit("나가기","quit"),;

        private final String name;
        private final String methodName;

        EFileMenuItem(String name, String methodName) {
            this.name = name;
            this.methodName = methodName;
        }
        public String getName(){
            return name;
        }

        public String getMethodName(){
            return methodName;
        }
    }

}
