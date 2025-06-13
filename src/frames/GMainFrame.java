package frames;

import global.GConstants;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;

import javax.swing.*;


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
		this.addEventHandler();
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
		this.setSize(GConstants.EMainFrame.eW.getValues(), GConstants.EMainFrame.eH.getValues());
		int x = GConstants.EMainFrame.eX.getValues();
		int y = GConstants.EMainFrame.eY.getValues();
		if (x == 0 && y == 0) {
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		} else {
			this.setLocation(x, y);
		}

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
				quit();
			}
		});
	}

	private void quit() {
		if(this.drawingPanel.isUpdated()) {
			int option = JOptionPane.showConfirmDialog(
					this,
					"변경된 내용이 있습니다. 저장하시겠습니까?",
					"종료 확인",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE
			);

			if (option == JOptionPane.YES_OPTION) {
				this.menuBar.getFileMenu().save();
				System.exit(0);
			} else if (option == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		} else {
			int option = JOptionPane.showConfirmDialog(
					this,
					"정말로 종료하시겠습니까?",
					"종료 확인",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE
			);

			if (option == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	public void initialize() {
		this.menuBar.associate(this.drawingPanel);
		this.toolBar.associate(this.drawingPanel);

		this.setVisible(true);
		this.menuBar.initialize();
		this.toolBar.initialize();
		this.drawingPanel.initialize();
	}
}