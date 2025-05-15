package transformers;

import java.awt.Graphics2D;
import shapes.GShape;

public abstract class GTransformer {

   protected GShape shape;

   public GTransformer(GShape shape) {
      this.shape = shape;
   }

   public abstract void start(int x, int y);
   public abstract void drag(int x, int y);
   public abstract void finish(int x, int y);
   public abstract void addPoint(int x, int y);
}