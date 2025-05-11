package frames;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JPanel;

import frames.GShapeToolBar.EShapeTool;
import shapes.GSelectRectangle;
import shapes.GShape;
import shapes.GShape.EPoints;
import transformers.GDrawer;
import transformers.GMover;
import transformers.GTransformer;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public enum EDrawingState {
		eIdle,
		e2P,
		eNP
	}

	private Vector<GShape> shapes;
	private GTransformer transformer;
	private GShape currentShape;
	private GShape selectedShape;
	private EShapeTool eShapeTool;
	private EDrawingState eDrawingState;

	public GDrawingPanel() {
		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		this.currentShape = null;
		this.selectedShape = null;
		this.shapes = new Vector<GShape>();
		this.eShapeTool = null;
		this.eDrawingState = EDrawingState.eIdle;
	}

	public void initialize() {
	}

	public void setEShapeTool(EShapeTool eShapeTool) {
		this.eShapeTool = eShapeTool;
	}

	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		for (GShape shape: this.shapes) {
			shape.draw((Graphics2D)graphics);
		}
	}

	private GShape onShape(int x, int y) {
		for (int i = this.shapes.size() - 1; i >= 0; i--) {
			GShape shape = this.shapes.get(i);
			if (shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	public void clearSelection() {
		for (GShape shape : this.shapes) {
			shape.setSelected(false);
		}
		this.selectedShape = null;
		this.repaint();
	}

	private void startTransform(int x, int y) {
		if (this.eShapeTool == EShapeTool.eSelect) {
			GShape clickedShape = onShape(x, y);

			if (clickedShape == null) {
				clearSelection();
				this.currentShape = new GSelectRectangle();
				this.shapes.add(this.currentShape);
				this.transformer = new GDrawer(this.currentShape);
			} else {
				if (!clickedShape.isSelected()) {
					clearSelection();
					clickedShape.setSelected(true);
					this.selectedShape = clickedShape;
					this.repaint();
				}
				this.transformer = new GMover(clickedShape);
			}
		} else {
			clearSelection();
			this.currentShape = eShapeTool.newShape();
			this.shapes.add(this.currentShape);
			this.transformer = new GDrawer(this.currentShape);
		}

		this.transformer.start((Graphics2D) getGraphics(), x, y);
	}

	private void keepTransform(int x, int y) {
		this.transformer.drag((Graphics2D) getGraphics(), x, y);
		this.repaint();
	}

	private void addPoint(int x, int y) {
		this.transformer.addPoint((Graphics2D) getGraphics(), x, y);
	}

	private void finishTransform(int x, int y) {
		this.transformer.finish((Graphics2D) getGraphics(), x, y);

		if (this.eShapeTool == EShapeTool.eSelect && this.currentShape instanceof GSelectRectangle) {
			GSelectRectangle selectRect = (GSelectRectangle) this.currentShape;
			Rectangle2D bounds = selectRect.getBounds();

			double x1 = bounds.getX();
			double y1 = bounds.getY();
			double x2 = x1 + bounds.getWidth();
			double y2 = y1 + bounds.getHeight();

			if (bounds.getWidth() < 0) {
				x1 = x2;
				x2 = bounds.getX();
			}

			if (bounds.getHeight() < 0) {
				y1 = y2;
				y2 = bounds.getY();
			}

			Rectangle2D normalizedBounds = new Rectangle2D.Double(
					Math.min(x1, x2),
					Math.min(y1, y2),
					Math.abs(bounds.getWidth()),
					Math.abs(bounds.getHeight())
			);

			if (normalizedBounds.getWidth() > 5 && normalizedBounds.getHeight() > 5) {
				for (GShape shape : this.shapes) {
					if (shape != selectRect) {
						if (shape.containedIn(normalizedBounds)) {
							shape.setSelected(true);
							this.selectedShape = shape;
						}
					}
				}
			}

			this.shapes.remove(this.currentShape);
		}

		this.repaint();
	}

	private class MouseHandler implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				this.mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				this.mouse2Clicked(e);
			}
		}

		private void mouse1Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (eShapeTool.getEPoints() == EPoints.e2P) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.e2P;
				} else if (eShapeTool.getEPoints() == EPoints.eNP) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNP;
				}
			} else if (eDrawingState == EDrawingState.e2P) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			} else if (eDrawingState == EDrawingState.eNP) {
				addPoint(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2P) {
				keepTransform(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eNP) {
				keepTransform(e.getX(), e.getY());
			}
		}

		private void mouse2Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eNP) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2P) {
				keepTransform(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eNP) {
				keepTransform(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}