package frames;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import global.GConstants;
import global.GConstants.EShapeTool;
import global.GConstants.EAnchor;
import global.GUndoRedoManager;
import global.GClipboard;
import global.GGraphicsProperties;
import shapes.GShape;
import shapes.GTextBox;
import shapes.GGroup;
import shapes.GShape.EPoints;
import transformers.*;

public class GDrawingPanel extends JPanel implements Printable {
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
	private GTextBox editingTextBox;
	private GUndoRedoManager undoRedoManager;
	private boolean ctrlPressed = false;
	private boolean isMultiTransform = false;
	private Map<GShape, AffineTransform> originalTransforms;
	private AffineTransform primaryOriginal;
	private double zoomLevel = 1.0;
	private AffineTransform viewTransform;
	private JScrollPane parentScrollPane;
	private static final int BASE_CANVAS_WIDTH = 800;
	private static final int BASE_CANVAS_HEIGHT = 600;

	public GDrawingPanel() {
		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		KeyHandler keyHandler = new KeyHandler();
		this.addKeyListener(keyHandler);
		this.setFocusable(true);

		this.currentShape = null;
		this.selectedShape = null;
		this.shapes = new Vector<GShape>();
		this.eShapeTool = null;
		this.eDrawingState = EDrawingState.eIdle;
		this.multiSelection = false;
		this.bUpdated = false;
		this.editingTextBox = null;
		this.undoRedoManager = new GUndoRedoManager();
		this.originalTransforms = new HashMap<>();
		this.viewTransform = new AffineTransform();

		MouseWheelHandler wheelHandler = new MouseWheelHandler();
		this.addMouseWheelListener(wheelHandler);
	}

	public void initialize() {
		this.shapes.clear();
		this.selectedShape = null;
		this.currentShape = null;
		this.editingTextBox = null;
		this.undoRedoManager.clear();
		saveCurrentState();
		this.repaint();
		this.removeAll();
	}

	public void setEShapeTool(EShapeTool eShapeTool) {
		this.eShapeTool = eShapeTool;
		stopTextEditing();
	}

	public boolean isUpdated() {
		return this.bUpdated;
	}

	public void setBUpdated(boolean bUpdated) {
		this.bUpdated = bUpdated;
	}

	public void saveCurrentState() {
		undoRedoManager.saveState(shapes);
	}

	public void undo() {
		Vector<GShape> previousState = undoRedoManager.undo();
		if (previousState != null) {
			this.shapes = previousState;
			clearSelection();
			stopTextEditing();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public void redo() {
		Vector<GShape> redoState = undoRedoManager.redo();
		if (redoState != null) {
			this.shapes = redoState;
			clearSelection();
			stopTextEditing();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public boolean canUndo() {
		return undoRedoManager.canUndo();
	}

	public boolean canRedo() {
		return undoRedoManager.canRedo();
	}

	public void copy() {
		List<GShape> selectedShapes = getSelectedShapes();
		if (!selectedShapes.isEmpty()) {
			GClipboard.getInstance().copy(selectedShapes);
		}
	}

	public void cut() {
		List<GShape> selectedShapes = getSelectedShapes();
		if (!selectedShapes.isEmpty()) {
			GClipboard.getInstance().copy(selectedShapes);
			for (GShape shape : selectedShapes) {
				shapes.remove(shape);
			}
			clearSelection();
			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public void paste() {
		List<GShape> pastedShapes = GClipboard.getInstance().paste();
		if (!pastedShapes.isEmpty()) {
			clearSelection();
			for (GShape shape : pastedShapes) {
				shapes.add(shape);
				shape.setSelected(true);
			}
			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public void delete() {
		List<GShape> selectedShapes = getSelectedShapes();
		if (!selectedShapes.isEmpty()) {
			for (GShape shape : selectedShapes) {
				shapes.remove(shape);
			}
			clearSelection();
			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public boolean hasSelection() {
		return !getSelectedShapes().isEmpty();
	}

	public boolean hasClipboardContent() {
		return GClipboard.getInstance().hasContent();
	}

	public void group() {
		List<GShape> selectedShapes = getSelectedShapes();
		if (selectedShapes.size() > 1) {
			// 선택된 도형들의 선택 상태 해제
			for (GShape shape : selectedShapes) {
				shape.setSelected(false);
				shapes.remove(shape);
			}

			// 그룹 생성 및 추가
			GGroup group = new GGroup(selectedShapes);
			shapes.add(group);
			group.setSelected(true);

			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public void ungroup() {
		List<GShape> selectedShapes = getSelectedShapes();
		List<GGroup> groupsToUngroup = new ArrayList<>();

		for (GShape shape : selectedShapes) {
			if (shape instanceof GGroup) {
				groupsToUngroup.add((GGroup) shape);
			}
		}

		if (!groupsToUngroup.isEmpty()) {
			for (GGroup group : groupsToUngroup) {
				group.setSelected(false);
				group.ungroup();
				List<GShape> children = group.getChildren();

				shapes.remove(group);

				for (GShape child : children) {
					shapes.add(child);
					child.setSelected(true);
				}
			}

			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public boolean canGroup() {
		return getSelectedShapes().size() > 1;
	}

	public boolean canUngroup() {
		List<GShape> selectedShapes = getSelectedShapes();
		for (GShape shape : selectedShapes) {
			if (shape instanceof GGroup) {
				return true;
			}
		}
		return false;
	}

	public void applyLineWidthToSelected(float lineWidth) {
		List<GShape> selectedShapes = getSelectedShapes();
		if (!selectedShapes.isEmpty()) {
			for (GShape shape : selectedShapes) {
				shape.setLineWidth(lineWidth);
			}
			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public void applyLineStyleToSelected(GGraphicsProperties.LineStyle lineStyle) {
		List<GShape> selectedShapes = getSelectedShapes();
		if (!selectedShapes.isEmpty()) {
			for (GShape shape : selectedShapes) {
				shape.setLineStyle(lineStyle);
			}
			saveCurrentState();
			this.bUpdated = true;
			this.repaint();
		}
	}

	public void printPanel() {
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		printerJob.setPrintable(this);

		if (printerJob.printDialog()) {
			try {
				printerJob.print();
			} catch (PrinterException e) {
				javax.swing.JOptionPane.showMessageDialog(this,
						"프린트 오류: " + e.getMessage(),
						"프린트 오류",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}

		Graphics2D g2d = (Graphics2D) graphics;

		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		double pageWidth = pageFormat.getImageableWidth();
		double pageHeight = pageFormat.getImageableHeight();
		double canvasWidth = BASE_CANVAS_WIDTH;
		double canvasHeight = BASE_CANVAS_HEIGHT;

		double scaleX = pageWidth / canvasWidth;
		double scaleY = pageHeight / canvasHeight;
		double scale = Math.min(scaleX, scaleY);

		g2d.scale(scale, scale);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, BASE_CANVAS_WIDTH, BASE_CANVAS_HEIGHT);

		for (GShape shape : this.shapes) {
			if (shape.isSelected()) {
				shape.setSelected(false);
				shape.draw(g2d);
				shape.setSelected(true);
			} else {
				shape.draw(g2d);
			}
		}

		return PAGE_EXISTS;
	}

	public void setParentScrollPane(JScrollPane scrollPane) {
		this.parentScrollPane = scrollPane;
	}

	public void zoomIn() {
		zoomLevel *= 1.2;
		updateViewTransform();
		updatePreferredSize();
		this.repaint();
	}

	public void zoomOut() {
		zoomLevel /= 1.2;
		updateViewTransform();
		updatePreferredSize();
		this.repaint();
	}

	public void resetZoom() {
		zoomLevel = 1.0;
		updateViewTransform();
		updatePreferredSize();
		this.repaint();
	}

	public void zoom(double factor, int centerX, int centerY) {
		if (parentScrollPane == null) return;

		// 현재 스크롤 위치
		int scrollX = parentScrollPane.getHorizontalScrollBar().getValue();
		int scrollY = parentScrollPane.getVerticalScrollBar().getValue();

		// 뷰포트 내에서의 마우스 위치
		int viewportX = centerX;
		int viewportY = centerY;

		// 캔버스 절대 좌표에서의 마우스 위치
		double canvasX = (scrollX + viewportX) / zoomLevel;
		double canvasY = (scrollY + viewportY) / zoomLevel;

		// 줌 레벨 변경
		double oldZoomLevel = zoomLevel;
		zoomLevel *= factor;
		if (zoomLevel < 0.1) zoomLevel = 0.1;
		if (zoomLevel > 10.0) zoomLevel = 10.0;

		updateViewTransform();
		updatePreferredSize();

		// 새로운 스크롤 위치 계산
		SwingUtilities.invokeLater(() -> {
			int newScrollX = (int)(canvasX * zoomLevel - viewportX);
			int newScrollY = (int)(canvasY * zoomLevel - viewportY);

			// 스크롤 범위 제한
			newScrollX = Math.max(0, Math.min(newScrollX, parentScrollPane.getHorizontalScrollBar().getMaximum() - parentScrollPane.getHorizontalScrollBar().getVisibleAmount()));
			newScrollY = Math.max(0, Math.min(newScrollY, parentScrollPane.getVerticalScrollBar().getMaximum() - parentScrollPane.getVerticalScrollBar().getVisibleAmount()));

			parentScrollPane.getHorizontalScrollBar().setValue(newScrollX);
			parentScrollPane.getVerticalScrollBar().setValue(newScrollY);
		});

		this.repaint();
	}

	private void updateViewTransform() {
		viewTransform.setToScale(zoomLevel, zoomLevel);
	}

	private void updatePreferredSize() {
		int newWidth = (int)(BASE_CANVAS_WIDTH * zoomLevel);
		int newHeight = (int)(BASE_CANVAS_HEIGHT * zoomLevel);

		this.setPreferredSize(new Dimension(newWidth, newHeight));
		this.revalidate();
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g2d = (Graphics2D) graphics;

		AffineTransform originalTransform = g2d.getTransform();
		g2d.transform(viewTransform);

		for (GShape shape: this.shapes) {
			shape.draw(g2d);
		}

		g2d.setTransform(originalTransform);

		if (editingTextBox != null && editingTextBox.isEditing()) {
			repaint();
		}
	}

	private GShape onShape(int x, int y) {
		Point2D worldPoint = new Point2D.Double(x, y);
		try {
			viewTransform.inverseTransform(worldPoint, worldPoint);
		} catch (Exception e) {
			return null;
		}

		for (int i = shapes.size() - 1; i >= 0; i--) {
			GShape shape = shapes.get(i);
			if (shape != null && shape.contains((int)worldPoint.getX(), (int)worldPoint.getY())) {
				return shape;
			}
		}
		return null;
	}

	private GTransformer createTransformerForShape(GShape shape) {
		EAnchor anchor = this.selectedShape.getESelectedAnchor();
		if (anchor.equals(EAnchor.eRR)) {
			return new GRotater(shape);
		} else if (anchor.equals(EAnchor.eMM)) {
			return new GMover(shape);
		} else {
			return new GResizer(shape);
		}
	}

	private void clearSelection() {
		for (GShape shape : this.shapes) {
			shape.setSelected(false);
		}
	}

	private List<GShape> getSelectedShapes() {
		List<GShape> selected = new ArrayList<>();
		for (GShape shape : this.shapes) {
			if (shape.isSelected()) {
				selected.add(shape);
			}
		}
		return selected;
	}

	private boolean hasMultipleSelection() {
		int count = 0;
		for (GShape shape : this.shapes) {
			if (shape.isSelected()) {
				count++;
				if (count > 1) return true;
			}
		}
		return false;
	}

	private void stopTextEditing() {
		if (editingTextBox != null) {
			editingTextBox.stopEditing();
			editingTextBox.setSelected(false);
			editingTextBox = null;
		}
	}

	private void startTransform(int x, int y) {
		Point2D worldPoint = new Point2D.Double(x, y);
		try {
			viewTransform.inverseTransform(worldPoint, worldPoint);
		} catch (Exception e) {
			return;
		}

		int worldX = (int)worldPoint.getX();
		int worldY = (int)worldPoint.getY();

		if (this.eShapeTool == EShapeTool.eSelect) {
			this.selectedShape = onShape(x, y);
			if (this.selectedShape == null) {
				this.currentShape = eShapeTool.newShape();
				this.shapes.add(this.currentShape);
				this.transformer = new GDrawer(this.currentShape);
				if (!ctrlPressed) {
					clearSelection();
				}
				stopTextEditing();
				multiSelection = true;
				isMultiTransform = false;
				this.transformer.start(worldX, worldY);
			} else {
				this.currentShape = null;
				if (!(this.selectedShape instanceof GTextBox) || !((GTextBox)this.selectedShape).isEditing()) {
					stopTextEditing();
				}

				if (ctrlPressed) {
					this.selectedShape.setSelected(!this.selectedShape.isSelected());
				} else {
					if (!this.selectedShape.isSelected()) {
						clearSelection();
						this.selectedShape.setSelected(true);
					}
				}

				isMultiTransform = hasMultipleSelection();

				if (isMultiTransform) {
					originalTransforms.clear();
					List<GShape> selectedShapes = getSelectedShapes();
					for (GShape shape : selectedShapes) {
						originalTransforms.put(shape, new AffineTransform(shape.getAffineTransform()));
					}
					primaryOriginal = new AffineTransform(this.selectedShape.getAffineTransform());
				}

				if(this.selectedShape.getESelectedAnchor().equals(EAnchor.eRR)) {
					this.transformer = new GRotater(this.selectedShape);
				} else if(this.selectedShape.getESelectedAnchor().equals(EAnchor.eMM)) {
					this.transformer = new GMover(this.selectedShape);
				} else {
					this.transformer = new GResizer(this.selectedShape);
				}
				this.transformer.start(worldX, worldY);
			}
		} else if (this.eShapeTool == EShapeTool.eTextBox) {
			this.currentShape = eShapeTool.newShape();
			this.shapes.add(this.currentShape);
			stopTextEditing();
			this.transformer = new GDrawer(this.currentShape);
			isMultiTransform = false;
			this.transformer.start(worldX, worldY);
		} else {
			this.currentShape = eShapeTool.newShape();
			this.shapes.add(this.currentShape);
			stopTextEditing();
			this.transformer = new GDrawer(this.currentShape);
			isMultiTransform = false;
			this.transformer.start(worldX, worldY);
		}
	}

	private void keepTransform(int x, int y) {
		Point2D worldPoint = new Point2D.Double(x, y);
		try {
			viewTransform.inverseTransform(worldPoint, worldPoint);
		} catch (Exception e) {
			return;
		}

		this.transformer.drag((int)worldPoint.getX(), (int)worldPoint.getY());

		if (isMultiTransform) {
			applyPrimaryTransformToOthers();
		}

		this.repaint();
	}

	private void applyPrimaryTransformToOthers() {
		AffineTransform currentPrimary = this.selectedShape.getAffineTransform();

		double[] originalMatrix = new double[6];
		double[] currentMatrix = new double[6];
		primaryOriginal.getMatrix(originalMatrix);
		currentPrimary.getMatrix(currentMatrix);

		// 이동량만 계산
		double deltaTranslateX = currentMatrix[4] - originalMatrix[4];
		double deltaTranslateY = currentMatrix[5] - originalMatrix[5];

		// 스케일 변화량 계산
		double originalScaleX = Math.sqrt(originalMatrix[0] * originalMatrix[0] + originalMatrix[1] * originalMatrix[1]);
		double originalScaleY = Math.sqrt(originalMatrix[2] * originalMatrix[2] + originalMatrix[3] * originalMatrix[3]);
		double currentScaleX = Math.sqrt(currentMatrix[0] * currentMatrix[0] + currentMatrix[1] * currentMatrix[1]);
		double currentScaleY = Math.sqrt(currentMatrix[2] * currentMatrix[2] + currentMatrix[3] * currentMatrix[3]);

		double scaleFactorX = currentScaleX / originalScaleX;
		double scaleFactorY = currentScaleY / originalScaleY;

		// 회전 변화량 계산
		double originalAngle = Math.atan2(originalMatrix[1], originalMatrix[0]);
		double currentAngle = Math.atan2(currentMatrix[1], currentMatrix[0]);
		double deltaAngle = currentAngle - originalAngle;

		EAnchor anchor = this.selectedShape.getESelectedAnchor();

		List<GShape> selectedShapes = getSelectedShapes();
		for (GShape otherShape : selectedShapes) {
			if (otherShape != this.selectedShape) {
				AffineTransform original = originalTransforms.get(otherShape);
				AffineTransform newTransform = new AffineTransform(original);

				if (anchor == EAnchor.eMM) {
					// 이동만 적용
					newTransform.translate(deltaTranslateX, deltaTranslateY);
				} else if (anchor == EAnchor.eRR) {
					// 회전만 적용 (각 도형의 중심 기준)
					double centerX = otherShape.getShape().getBounds2D().getCenterX();
					double centerY = otherShape.getShape().getBounds2D().getCenterY();
					newTransform.rotate(deltaAngle, centerX, centerY);
				} else {
					// 리사이징만 적용 (각 도형의 중심 기준)
					double centerX = otherShape.getShape().getBounds2D().getCenterX();
					double centerY = otherShape.getShape().getBounds2D().getCenterY();
					newTransform.translate(centerX, centerY);
					newTransform.scale(scaleFactorX, scaleFactorY);
					newTransform.translate(-centerX, -centerY);
				}

				otherShape.getAffineTransform().setTransform(newTransform);
			}
		}
	}

	private void addPoint(int x, int y) {
		Point2D worldPoint = new Point2D.Double(x, y);
		try {
			viewTransform.inverseTransform(worldPoint, worldPoint);
		} catch (Exception e) {
			return;
		}

		this.transformer.addPoint((int)worldPoint.getX(), (int)worldPoint.getY());
		this.repaint();
	}

	private void finishTransform(int x, int y) {
		Point2D worldPoint = new Point2D.Double(x, y);
		try {
			viewTransform.inverseTransform(worldPoint, worldPoint);
		} catch (Exception e) {
			return;
		}

		if (!isMultiTransform && this.transformer != null) {
			this.transformer.finish((int)worldPoint.getX(), (int)worldPoint.getY());
		}

		if (this.eShapeTool == GConstants.EShapeTool.eSelect) {
			if (multiSelection) {
				this.selectShapesInArea();
				if (this.currentShape != null) {
					this.shapes.remove(this.currentShape);
				}
				multiSelection = false;
			} else {
				saveCurrentState();
			}
		} else if (this.eShapeTool == EShapeTool.eTextBox) {
			this.selectShape();
			this.editingTextBox = (GTextBox) this.currentShape;
			this.editingTextBox.startEditing();
			this.requestFocus();
			saveCurrentState();
		} else {
			this.selectShape();
			saveCurrentState();
		}

		isMultiTransform = false;
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
		this.editingTextBox = null;
		this.undoRedoManager.clear();
		saveCurrentState();
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
				if (this.selectedShape.isSelected()) {
					EAnchor eAnchor = this.selectedShape.getESelectedAnchor();
					if (eAnchor != null) {
						this.setCursor(eAnchor.getCursor());
					} else {
						this.setCursor(Cursor.getDefaultCursor());
					}
				} else {
					this.setCursor(Cursor.getDefaultCursor());
				}
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
			} else if (eDrawingState == EDrawingState.eIdle && eShapeTool == EShapeTool.eSelect) {
				GShape clickedShape = onShape(e.getX(), e.getY());
				if (clickedShape instanceof GTextBox) {
					stopTextEditing();
					clearSelection();
					clickedShape.setSelected(true);
					editingTextBox = (GTextBox) clickedShape;
					editingTextBox.startEditing();
					requestFocus();
					repaint();
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			requestFocus();
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

	private class KeyHandler implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			if (editingTextBox != null) {
				char c = e.getKeyChar();
				if (c != KeyEvent.CHAR_UNDEFINED && c >= 32) {
					editingTextBox.insertText(String.valueOf(c));
					bUpdated = true;
					repaint();
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				ctrlPressed = true;
			}

			// 다중 선택된 도형들 회전 테스트용
			if (e.getKeyCode() == KeyEvent.VK_R && hasMultipleSelection()) {
				List<GShape> selectedShapes = getSelectedShapes();
				for (GShape shape : selectedShapes) {
					double centerX = shape.getShape().getBounds2D().getCenterX();
					double centerY = shape.getShape().getBounds2D().getCenterY();
					shape.getAffineTransform().rotate(Math.toRadians(15), centerX, centerY);
				}
				repaint();
				return;
			}

			if (editingTextBox != null) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_ENTER:
						if (e.isShiftDown()) {
							editingTextBox.insertNewLine();
							bUpdated = true;
							repaint();
						} else {
							saveCurrentState();
							stopTextEditing();
							repaint();
						}
						break;
					case KeyEvent.VK_BACK_SPACE:
						editingTextBox.deleteChar();
						bUpdated = true;
						repaint();
						break;
					case KeyEvent.VK_LEFT:
						editingTextBox.moveCursor(-1);
						repaint();
						break;
					case KeyEvent.VK_RIGHT:
						editingTextBox.moveCursor(1);
						repaint();
						break;
					case KeyEvent.VK_ESCAPE:
						saveCurrentState();
						stopTextEditing();
						repaint();
						break;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				ctrlPressed = false;
			}
		}
	}

	private class MouseWheelHandler implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (ctrlPressed) {
				int rotation = e.getWheelRotation();
				if (rotation < 0) {
					zoom(1.1, e.getX(), e.getY());
				} else {
					zoom(0.9, e.getX(), e.getY());
				}
			}
		}
	}
}