package shapes;

import java.awt.geom.QuadCurve2D;

public class GQuadCurve extends GShape {
    private QuadCurve2D quadCurve;
    private double ctrlX, ctrlY;
    private boolean firstPointSet = false;
    private int pointCount = 0;

    public GQuadCurve() {
        super(new QuadCurve2D.Float(0, 0, 0, 0, 0, 0));
        this.quadCurve = (QuadCurve2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        // 시작점 설정
        this.pointCount = 1;
        this.quadCurve.setCurve(x, y, x, y, x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        double x1 = quadCurve.getX1();
        double y1 = quadCurve.getY1();

        if (pointCount == 1) {
            // 첫 번째 점 후 드래그 - 끝점 이동
            this.quadCurve.setCurve(x1, y1, (x1 + x) / 2, (y1 + y) / 2, x, y);
        } else if (pointCount == 2) {
            // 두 번째 점(컨트롤 포인트) 지정 후 드래그 - 컨트롤 포인트만 이동
            double x2 = quadCurve.getX2();
            double y2 = quadCurve.getY2();
            this.quadCurve.setCurve(x1, y1, x, y, x2, y2);
        }
    }

    @Override
    public void addPoint(int x, int y) {
        double x1 = quadCurve.getX1();
        double y1 = quadCurve.getY1();

        if (pointCount == 1) {
            // 두 번째 클릭 - 컨트롤 포인트 설정
            double x2 = quadCurve.getX2();
            double y2 = quadCurve.getY2();
            this.quadCurve.setCurve(x1, y1, x, y, x2, y2);
            pointCount = 2;
        }
    }
}