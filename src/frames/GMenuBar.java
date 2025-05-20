package frames;

import menus.GFileMenu;

import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar implements GComponent {
	private static final long serialVersionUID = 1L;
	//components
	private GFileMenu fileMenu;

	//associations
	private GDrawingPanel drawingPanel;

	public GMenuBar() {
		this.fileMenu = new GFileMenu();
		this.add(fileMenu);
	}

	@Override
	public void createComponents() {

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

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
}
