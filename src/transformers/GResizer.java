package transformers;

import global.GConstants;
import shapes.GShape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static global.GConstants.*;

public class GResizer extends GTransformer {

    private AffineTransform initialTransformAtStart; // 리사이즈 시작 시 도형의 변환
    private Point2D.Double worldPivotPoint;         // 고정점 (월드 좌표)
    private Point2D.Double worldDragStartPoint;     // 드래그 시작점 (월드 좌표)
    private EAnchor selectedAnchor;                 // 선택된 앵커
    private Rectangle2D initialTransformedBounds;   // 리사이즈 시작 시 변환된 경계
    private double initialRotation;                 // 초기 회전 각도 (라디안)

    public GResizer(GShape shape) {
        super(shape);
        this.worldPivotPoint = new Point2D.Double();
        this.worldDragStartPoint = new Point2D.Double();
    }

    @Override
    public void start(int x, int y) {
        this.selectedAnchor = shape.getESelectedAnchor();
        if (this.selectedAnchor == null || this.selectedAnchor == EAnchor.eMM || this.selectedAnchor == EAnchor.eRR) {
            this.selectedAnchor = null;
            return;
        }

        this.initialTransformAtStart = new AffineTransform(shape.getAffineTransform());
        this.initialTransformedBounds = shape.getTransformedShape().getBounds2D();
        this.worldDragStartPoint.setLocation(x, y);

        // 초기 회전 각도 추출
        double[] matrix = new double[6];
        initialTransformAtStart.getMatrix(matrix);
        this.initialRotation = Math.atan2(matrix[1], matrix[0]);

        // 앵커에 따른 피벗점 설정 (반대쪽 앵커를 피벗점으로)
        double minX = initialTransformedBounds.getMinX();
        double minY = initialTransformedBounds.getMinY();
        double midX = initialTransformedBounds.getCenterX();
        double midY = initialTransformedBounds.getCenterY();
        double maxX = initialTransformedBounds.getMaxX();
        double maxY = initialTransformedBounds.getMaxY();

        switch (this.selectedAnchor) {
            case eNW: this.worldPivotPoint.setLocation(maxX, maxY); break; // SE가 피벗
            case eNN: this.worldPivotPoint.setLocation(midX, maxY); break; // SS가 피벗
            case eNE: this.worldPivotPoint.setLocation(minX, maxY); break; // SW가 피벗
            case eWW: this.worldPivotPoint.setLocation(maxX, midY); break; // EE가 피벗
            case eEE: this.worldPivotPoint.setLocation(minX, midY); break; // WW가 피벗
            case eSW: this.worldPivotPoint.setLocation(maxX, minY); break; // NE가 피벗
            case eSS: this.worldPivotPoint.setLocation(midX, minY); break; // NN가 피벗
            case eSE: this.worldPivotPoint.setLocation(minX, minY); break; // NW가 피벗
            default:
                this.selectedAnchor = null;
                return;
        }
    }

    @Override
    public void drag(int x, int y) {
        if (this.selectedAnchor == null || this.initialTransformAtStart == null || this.initialTransformedBounds == null) {
            return;
        }

        // 현재 마우스 위치
        Point2D.Double currentPoint = new Point2D.Double(x, y);

        // 초기 변환의 역변환으로 로컬 좌표계로 변환
        AffineTransform inverseTransform;
        try {
            inverseTransform = initialTransformAtStart.createInverse();
        } catch (Exception e) {
            return; // 역변환이 불가능한 경우
        }

        Point2D.Double localPivot = new Point2D.Double();
        Point2D.Double localCurrent = new Point2D.Double();
        inverseTransform.transform(this.worldPivotPoint, localPivot);
        inverseTransform.transform(currentPoint, localCurrent);

        // GShape의 keepResize 로직을 참고하여 스케일링 비율 계산
        double minX = initialTransformedBounds.getMinX();
        double minY = initialTransformedBounds.getMinY();
        double maxX = initialTransformedBounds.getMaxX();
        double maxY = initialTransformedBounds.getMaxY();
        double width = maxX - minX;
        double height = maxY - minY;

        double scaleX = 1.0;
        double scaleY = 1.0;

        // 앵커에 따라 스케일링 비율 계산 (GShape의 keepResize 방식 반영)
        switch (this.selectedAnchor) {
            case eNW:
                scaleX = Math.abs(maxX - x) / Math.abs(maxX - minX);
                scaleY = Math.abs(maxY - y) / Math.abs(maxY - minY);
                break;
            case eNN:
                scaleX = 1.0;
                scaleY = Math.abs(maxY - y) / Math.abs(maxY - minY);
                break;
            case eNE:
                scaleX = Math.abs(x - minX) / Math.abs(maxX - minX);
                scaleY = Math.abs(maxY - y) / Math.abs(maxY - minY);
                break;
            case eEE:
                scaleX = Math.abs(x - minX) / Math.abs(maxX - minX);
                scaleY = 1.0;
                break;
            case eSE:
                scaleX = Math.abs(x - minX) / Math.abs(maxX - minX);
                scaleY = Math.abs(y - minY) / Math.abs(maxY - minY);
                break;
            case eSS:
                scaleX = 1.0;
                scaleY = Math.abs(y - minY) / Math.abs(maxY - minY);
                break;
            case eSW:
                scaleX = Math.abs(maxX - x) / Math.abs(maxX - minX);
                scaleY = Math.abs(y - minY) / Math.abs(maxY - minY);
                break;
            case eWW:
                scaleX = Math.abs(maxX - x) / Math.abs(maxX - minX);
                scaleY = 1.0;
                break;
            default:
                return;
        }

        // 최소 스케일 제한
        final double MIN_ABS_SCALE = 0.01;
        if (Math.abs(scaleX) < MIN_ABS_SCALE) {
            scaleX = MIN_ABS_SCALE * Math.signum(scaleX);
        }
        if (Math.abs(scaleY) < MIN_ABS_SCALE) {
            scaleY = MIN_ABS_SCALE * Math.signum(scaleY);
        }

        // 새로운 변환 생성
        AffineTransform newTransform = new AffineTransform();
        // 1. 피벗점으로 이동
        newTransform.translate(this.worldPivotPoint.getX(), this.worldPivotPoint.getY());
        // 2. 초기 회전 역방향 적용 (로컬 좌표계로)
        newTransform.rotate(-this.initialRotation);
        // 3. 스케일링 적용
        newTransform.scale(scaleX, scaleY);
        // 4. 초기 회전 복원
        newTransform.rotate(this.initialRotation);
        // 5. 피벗점으로 되돌아감
        newTransform.translate(-this.worldPivotPoint.getX(), -this.worldPivotPoint.getY());
        // 6. 초기 변환 적용
        newTransform.concatenate(this.initialTransformAtStart);

        // 도형에 새로운 변환 적용
        this.shape.getAffineTransform().setTransform(newTransform);
    }

    @Override
    public void finish(int x, int y) {
        // 상태 초기화 (필요 시)
    }

    @Override
    public void addPoint(int x, int y) {
        // 사용되지 않음
    }
}