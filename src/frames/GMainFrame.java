package frames;

import global.GConstants;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;

import javax.swing.*;

import static global.GConstants.GMainFrame.WIDTH;

public class GMainFrame extends JFrame implements GComponent {
	@Serial
	private static final long serialVersionUID = 1L;
	private GMenuBar menuBar;
	private GShapeToolBar toolBar;
	private GDrawingPanel drawingPanel;
	
	public GMainFrame(){
		this.setAttributes();
		this.createComponents();
		this.arrangeComponents();
	}

	@Override
	public void createComponents() {
		this.menuBar = new GMenuBar();
		this.setJMenuBar(menuBar);
		this.toolBar = new GShapeToolBar();
		this.drawingPanel = new GDrawingPanel();
	}

	@Override
	public void setAttributes() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(GConstants.GMainFrame.WIDTH, GConstants.GMainFrame.HEIGHT);
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

	}

	@Override
	public void arrangeComponents() {
		this.add(toolBar, BorderLayout.NORTH);
		this.add(drawingPanel, BorderLayout.CENTER);
	}

	@Override
	public void addEventHandler() {

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(!GMainFrame.this.drawingPanel.isUpdated()){
					System.exit(0);
				}
				int option = JOptionPane.showConfirmDialog(
						GMainFrame.this,
						"정말로 종료하시겠습니까?", // 메시지
						"종료 확인", // 다이얼로그 제목
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

				if (option == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else {
					//
				}
			}
		});
		//
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
