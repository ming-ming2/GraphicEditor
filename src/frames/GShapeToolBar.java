package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GSelectRectangle;
import shapes.GShape;
import shapes.GShape.EPoints;

public class GShapeToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	public enum EShapeTool {
		eSelect("select", EPoints.e2P,  GSelectRectangle.class),
		eRectnalge("rectangle", EPoints.e2P, GRectangle.class),
		eEllipse("ellipse", EPoints.e2P, GRectangle.class),
		eLine("line", EPoints.e2P, GRectangle.class),
		ePolygon("polygon", EPoints.eNP, GPolygon.class);

		private String name;
		private EPoints ePoints;
		private Class<?> classShape;
		private EShapeTool(String name, EPoints ePoints, Class<?> classShape) {
			this.name = name;
			this.ePoints = ePoints;
			this.classShape = classShape;
		}
		public String getName() {
			return this.name;
		}
		public EPoints getEPoints() {
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
	}	// components

	// associations
	private GDrawingPanel drawingPanel;

	public GShapeToolBar() {
		ButtonGroup buttonGroup = new ButtonGroup();
		for (EShapeTool eShpaeType: EShapeTool.values()) {
			JRadioButton radioButton = new JRadioButton(eShpaeType.getName());
			ActionHandler actionHandler = new ActionHandler();
			radioButton.addActionListener(actionHandler);
			radioButton.setActionCommand(eShpaeType.toString());

			buttonGroup.add(radioButton);
			this.add(radioButton);
		}
	}

	public void initialize() {
		JRadioButton button = (JRadioButton) this.getComponent(EShapeTool.eSelect.ordinal());
		button.doClick();
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String sShapeType = e.getActionCommand();
			EShapeTool eShapeType = EShapeTool.valueOf(sShapeType);
			drawingPanel.setEShapeTool(eShapeType);
		}
	}
}