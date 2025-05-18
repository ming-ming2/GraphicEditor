package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public abstract class GShape {
    private final static int ANCHOR_WIDTH = 10;
    private final static int ANCHOR_HEIGHT = 10;
    private final AffineTransform affineTransform;



    public enum EPoints {
        e2P,
        eNP;
    }
    public enum EAnchor {
        eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNE(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNW(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSE(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSW(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
        eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
        eRR(new Cursor(Cursor.HAND_CURSOR)),
        eMM(new Cursor(Cursor.HAND_CURSOR));

        private Cursor cursor;

        EAnchor(Cursor cursor) {
            this.cursor = cursor;
        }
        public Cursor getCursor() {
            return this.cursor;
        }

    }
    private enum ECursor {

    }
    private Shape shape;

    private Ellipse2D[] anchors;
    private boolean bSelected;
    private EAnchor eSelectedAnchor;
    private int px,py;
    public GShape(Shape shape) {
        this.shape = shape;
        this.anchors = new Ellipse2D[EAnchor.values().length-1];
        for(int i = 0; i < this.anchors.length; i++){
            this.anchors[i] = new Ellipse2D.Double();
        }
        this.bSelected = false;
        this.eSelectedAnchor = null;
        this.affineTransform = new AffineTransform();
    }

    //getters and setters

    public Shape getShape() {
        return this.shape;
    }
    public boolean isSelected() {
        return this.bSelected;
    }

    public void setSelected(boolean bSelected) {
        this.bSelected = bSelected;
    }
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }
    public EAnchor getESelectedAnchor() {
        return this.eSelectedAnchor;
    }

    private void setAnchors(){
        Rectangle bounds = this.shape.getBounds();
        int bx = bounds.x;
        int by = bounds.y;
        int bw = bounds.width;
        int bh = bounds.height;

        int cx = 0;
        int cy = 0;
        for(int i = 0; i < this.anchors.length; i++){
            switch(EAnchor.values()[i]){
                case eNN:
                    cx = bx + bw/2; cy = by; break;
                case eNE:
                    cx = bx + bw; cy = by; break;
                case eNW:
                    cx = bx; cy = by; break;
                case eSS:
                    cx = bx + bw/2; cy = by + bh; break;
                case eSE:
                    cx = bx + bw; cy = by + bh; break;
                case eSW:
                    cx = bx; cy = by+bh; break;
                case eEE:
                    cx = bx + bw; cy = by + bh/2; break;
                case eWW:
                    cx = bx; cy = by + bh/2; break;
                case eRR:
                    cx = bx + bw/2; cy = by -30; break;
            }

            anchors[i].setFrame(cx - (double) ANCHOR_WIDTH /2, cy - (double) ANCHOR_HEIGHT /2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
        }
    }


    public void draw(Graphics2D graphics2D) {
        Shape transformedShape = this.affineTransform.createTransformedShape(shape);
        graphics2D.draw(transformedShape);
        if(bSelected){
            this.setAnchors();
            for(Ellipse2D anchor : this.anchors){
                Color penColor = graphics2D.getColor();
                Shape transformedAnchor = this.affineTransform.createTransformedShape(anchor);
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(transformedAnchor);
                graphics2D.setColor(penColor);
                graphics2D.draw(transformedAnchor);
            }
        }
    }

    public boolean contains(int x, int y) {
        if(bSelected) {
            for(int i = 0; i < this.anchors.length; i++) {
                Shape transformedAnchor = this.affineTransform.createTransformedShape(anchors[i]);
                if(transformedAnchor.contains(x, y)) {
                    this.eSelectedAnchor = EAnchor.values()[i];
                    return true;
                }
            }
        }

        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        if(transformedShape.contains(x, y)) {
            this.eSelectedAnchor = EAnchor.eMM;
            return true;
        }
        return false;
    }


    public void translate(int dx, int dy) {
        this.affineTransform.translate(dx,dy);
    }

    public void scale(double scaleX, double scaleY) {
        this.affineTransform.scale(scaleX,scaleY);
    }

   public void rotate(double theta){
       this.affineTransform.rotate(theta);
   }

   public double getWidth(){
        return this.shape.getBounds2D().getWidth();
   }

   public double getHeight(){
        return this.shape.getBounds2D().getHeight();
   }


    //draw
    public abstract void addPoint(int x, int y);
    public abstract void setPoint(int x, int y);
    public abstract void dragPoint(int x, int y);

}
