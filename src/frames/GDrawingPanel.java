package frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import frames.GShapeToolBar.EShapeTool;
import shapes.GShape;
import shapes.GShape.EPoints;
import transformers.*;

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
		for (GShape shape: this.shapes) {
			if (shape!=null && shape.contains(x, y)) {
				shape.setSelected(true);
				return shape;
			}
		}
		return null;
	}

	private void startTransform(int x, int y) {
		// set shape
		this.currentShape = eShapeTool.newShape();
		this.shapes.add(this.currentShape);
		if (this.eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);
			if (this.selectedShape == null) {
				this.transformer = new GDrawer(this.currentShape);
			} else {
				if(this.selectedShape.getESelectedAnchor().equals(GShape.EAnchor.eRR)) {
					this.transformer = new GRotate(this.selectedShape);
				} else if(this.selectedShape.getESelectedAnchor() != null){
					this.transformer = new GResize(this.selectedShape);
				} else {
					this.transformer = new GMover(this.selectedShape);
				}
			}
		} else {
			this.transformer = new GDrawer(this.currentShape);
		}
		this.transformer.start(x, y);
	}
	private void keepTransform(int x, int y) {
		this.transformer.drag(x, y);
		this.repaint();
	}
	private void addPoint(int x, int y) {
		this.transformer.addPoint(x, y);
	}
	private void finishTransform(int x, int y) {
		this.transformer.finish(x, y);
		this.selectShape();
		if (this.eShapeTool == EShapeTool.eSelect) {
			this.shapes.removeLast();
		}
		this.repaint();
	}

	private void selectShape() {
		for(GShape shape: this.shapes) {
			shape.setSelected(false);
		}
		this.currentShape.setSelected(true);
	}

	private void changeCursor(int x, int y) {
		this.selectedShape = onShape(x, y);
		if(this.selectedShape == null) {
			this.setCursor(Cursor.getDefaultCursor());
		} else {
			GShape.EAnchor eAnchor = this.selectedShape.getESelectedAnchor();
			this.setCursor(eAnchor.getCursor());
		}
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
				// set transformer
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
			} else if(eDrawingState == EDrawingState.eIdle){
				changeCursor(e.getX(),e.getY());
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