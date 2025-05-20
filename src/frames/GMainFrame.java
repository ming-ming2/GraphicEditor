package frames;

import global.GConstants;

import java.awt.*;
import java.io.Serial;

import javax.swing.JFrame;

import static global.GConstants.GMainFrame.WIDTH;

public class GMainFrame extends JFrame {
	@Serial
	private static final long serialVersionUID = 1L;
	private GMenuBar menuBar;
	private GShapeToolBar toolBar;
	private GDrawingPanel drawingPanel;
	
	public GMainFrame(){
		//attributes
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(GConstants.GMainFrame.WIDTH, GConstants.GMainFrame.HEIGHT);
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		//components
		this.menuBar = new GMenuBar();
		this.setJMenuBar(menuBar);
		
		this.toolBar = new GShapeToolBar();
		this.add(toolBar, BorderLayout.NORTH);
		this.drawingPanel = new GDrawingPanel();
		this.add(drawingPanel, BorderLayout.CENTER);

	}

	public void initialize() {
		//associate
		this.menuBar.associate(this.drawingPanel);
		this.toolBar.associate(this.drawingPanel);

		//associated attributes
		this.setVisible(true);
		this.menuBar.initialize();
		this.toolBar.initialize();
		this.drawingPanel.initialize();
	}
}
