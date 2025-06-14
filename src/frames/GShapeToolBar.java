package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import global.GConstants;

public class GShapeToolBar extends JToolBar implements GComponent {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawingPanel;

	public GShapeToolBar() {
		this.createComponents();
		this.setAttributes();
		this.arrangeComponents();
		this.addEventHandler();
	}

	@Override
	public void createComponents() {
		ButtonGroup buttonGroup = new ButtonGroup();
		ActionHandler actionHandler = new ActionHandler();

		for (GConstants.EShapeTool eShapeTool : GConstants.EShapeTool.values()) {
			JRadioButton radioButton = new JRadioButton(eShapeTool.getName());
			radioButton.addActionListener(actionHandler);
			radioButton.setActionCommand(eShapeTool.toString());

			if (eShapeTool.getName() != null && !eShapeTool.getName().isEmpty()) {
				radioButton.setToolTipText(eShapeTool.getName());
			}

			buttonGroup.add(radioButton);
			this.add(radioButton);
		}
	}

	@Override
	public void setAttributes() {
	}

	@Override
	public void arrangeComponents() {
	}

	@Override
	public void addEventHandler() {
	}

	@Override
	public void initialize() {
		JRadioButton button = (JRadioButton) this.getComponent(GConstants.EShapeTool.eSelect.ordinal());
		button.doClick();
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String sShapeType = e.getActionCommand();
			GConstants.EShapeTool eShapeType = GConstants.EShapeTool.valueOf(sShapeType);
			drawingPanel.setEShapeTool(eShapeType);
		}
	}
}