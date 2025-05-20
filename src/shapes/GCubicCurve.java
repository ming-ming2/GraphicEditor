package shapes;

import java.awt.geom.CubicCurve2D;

public class GCubicCurve extends GShape {
    private CubicCurve2D cubicCurve;
    private int pointCount = 0;

    public GCubicCurve() {
        super(new CubicCurve2D.Float(0, 0, 0, 0, 0, 0, 0, 0));
        this.cubicCurve = (CubicCurve2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        // 시작점 설정
        pointCount = 1;
        this.cubicCurve.setCurve(x, y, x, y, x, y, x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        double x1 = cubicCurve.getX1();
        double y1 = cubicCurve.getY1();

        if (pointCount == 1) {
            // 첫 번째 점 후 드래그 - 끝점 이동
            double ctrlX1 = x1 + (x - x1) / 3;
            double ctrlY1 = y1 + (y - y1) / 3;
            double ctrlX2 = x1 + 2 * (x - x1) / 3;
            double ctrlY2 = y1 + 2 * (y - y1) / 3;
            this.cubicCurve.setCurve(x1, y1, ctrlX1, ctrlY1, ctrlX2, ctrlY2, x, y);
        } else if (pointCount == 2) {
            // 두 번째 점(첫 번째 컨트롤 포인트) 지정 후 드래그
            double ctrlX1 = cubicCurve.getCtrlX1();
            double ctrlY1 = cubicCurve.getCtrlY1();
            double x2 = cubicCurve.getX2();
            double y2 = cubicCurve.getY2();
            this.cubicCurve.setCurve(x1, y1, ctrlX1, ctrlY1, x, y, x2, y2);
        } else if (pointCount == 3) {
            // 세 번째 점(두 번째 컨트롤 포인트) 지정 후 드래그
            double ctrlX1 = cubicCurve.getCtrlX1();
            double ctrlY1 = cubicCurve.getCtrlY1();
            double ctrlX2 = cubicCurve.getCtrlX2();
            double ctrlY2 = cubicCurve.getCtrlY2();
            double x2 = cubicCurve.getX2();
            double y2 = cubicCurve.getY2();
            this.cubicCurve.setCurve(x1, y1, ctrlX1, ctrlY1, ctrlX2, ctrlY2, x, y);
        }
    }

    @Override
    public void addPoint(int x, int y) {
        double x1 = cubicCurve.getX1();
        double y1 = cubicCurve.getY1();
        double x2 = cubicCurve.getX2();
        double y2 = cubicCurve.getY2();

        if (pointCount == 1) {
            // 두 번째 클릭 - 첫 번째 컨트롤 포인트 설정
            double ctrlX2 = cubicCurve.getCtrlX2();
            double ctrlY2 = cubicCurve.getCtrlY2();
            this.cubicCurve.setCurve(x1, y1, x, y, ctrlX2, ctrlY2, x2, y2);
            pointCount = 2;
        } else if (pointCount == 2) {
            // 세 번째 클릭 - 두 번째 컨트롤 포인트 설정
            double ctrlX1 = cubicCurve.getCtrlX1();
            double ctrlY1 = cubicCurve.getCtrlY1();
            this.cubicCurve.setCurve(x1, y1, ctrlX1, ctrlY1, x, y, x2, y2);
            pointCount = 3;
        }
    }
}