package frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import global.GConstants;
import global.GConstants.EShapeTool;
import global.GConstants.EAnchor;
import shapes.GShape;
import shapes.GShape.EPoints;
import transformers.*;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;


	public enum EDrawingState {
		eIdle,
		e2P,
		eNP;

	}
	private Vector<GShape> shapes;
	private GTransformer transformer;

	private GShape currentShape;
	private GShape selectedShape;
	private EShapeTool eShapeTool;
	private EDrawingState eDrawingState;
	private boolean multiSelection;
	private boolean bUpdated;
	public GDrawingPanel() {
		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		this.currentShape = null;
		this.selectedShape = null;
		this.shapes = new Vector<GShape>();
		this.eShapeTool = null;
		this.eDrawingState = EDrawingState.eIdle;
		this.multiSelection = false;
		this.bUpdated = false;
	}
	public void initialize() {
		this.shapes.clear();
		this.selectedShape = null;
		this.currentShape = null;
		this.repaint();
		this.removeAll();
	}

	public void setEShapeTool(EShapeTool eShapeTool) {
		this.eShapeTool = eShapeTool;
	}

	public boolean isUpdated() {
		return this.bUpdated;
	}
	public void setBUpdated(boolean bUpdated) {
		this.bUpdated = bUpdated;
	}

	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		for (GShape shape: this.shapes) {
			shape.draw((Graphics2D)graphics);
		}
	}

	private GShape onShape(int x, int y) {
		for (int i = shapes.size() - 1; i >= 0; i--) {
			GShape shape = shapes.get(i);
			if (shape != null && shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	private void clearSelection() {
		for (GShape shape : this.shapes) {
			shape.setSelected(false);
		}
	}

	private void startTransform(int x, int y) {
		this.currentShape = eShapeTool.newShape();
		this.shapes.add(this.currentShape);

		if (this.eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);
			if (this.selectedShape == null) {
				this.transformer = new GDrawer(this.currentShape);
				clearSelection();
				multiSelection = true;
			} else {
				if (!this.selectedShape.isSelected()) {
					clearSelection();
					this.selectedShape.setSelected(true);
				}

				if(this.selectedShape.getESelectedAnchor().equals(EAnchor.eRR)) {
					this.transformer = new GRotater(this.selectedShape);
				} else if(this.selectedShape.getESelectedAnchor().equals(EAnchor.eMM)) {
					this.transformer = new GMover(this.selectedShape);
				} else {
					this.transformer = new GResizer(this.selectedShape);
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
		this.repaint();
	}

	private void finishTransform(int x, int y) {
		this.transformer.finish(x, y);

		if (this.eShapeTool == GConstants.EShapeTool.eSelect) {
			if (multiSelection) {
				this.selectShapesInArea();
				this.shapes.removeLast();
				multiSelection = false;
			}
		} else {
			this.selectShape();
		}
//		this.bUpdated = this.transformer.isUpdated();
		this.bUpdated = true;
		this.repaint();
	}

	private void selectShape() {
		clearSelection();
		this.currentShape.setSelected(true);
	}
	public Vector<GShape> getShapes() {
		return this.shapes;
	}

	public void setShapes(Vector<GShape> shapes) {
		this.shapes = shapes;
		this.repaint();
	}


	private void selectShapesInArea() {
		Rectangle selectionArea = this.currentShape.getTransformedShape().getBounds();
		for (GShape shape : this.shapes) {
			if (shape != this.currentShape) {
				if (selectionArea.contains(shape.getTransformedShape().getBounds())) {
					shape.setSelected(true);
				}
			}
		}
	}

	private void changeCursor(int x, int y) {
		if(this.eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);
			if(this.selectedShape == null) {
				this.setCursor(Cursor.getDefaultCursor());
			} else {
				EAnchor eAnchor = this.selectedShape.getESelectedAnchor();
				this.setCursor(eAnchor.getCursor());
			}
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