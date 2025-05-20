package menus;

import frames.GComponent;
import frames.GDrawingPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GFileMenu extends JMenu implements GComponent {
	private static final long serialVersionUID = 1L;

	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem loadImageMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveAsImageMenuItem;
	private JMenuItem exitMenuItem;

	private GDrawingPanel drawingPanel;

	public GFileMenu() {
		super("File");
		this.createComponents();
		this.setAttributes();
		this.arrangeComponents();
		this.addEventHandler();
	}

	@Override
	public void createComponents() {
		newMenuItem = new JMenuItem("New");
		openMenuItem = new JMenuItem("Open");
		loadImageMenuItem = new JMenuItem("Load Background Image");
		saveMenuItem = new JMenuItem("Save");
		saveAsMenuItem = new JMenuItem("Save As");
		saveAsImageMenuItem = new JMenuItem("Export as Image");
		exitMenuItem = new JMenuItem("Exit");
	}

	@Override
	public void arrangeComponents() {
		this.add(newMenuItem);
		this.add(openMenuItem);
		this.add(loadImageMenuItem);
		this.add(new JSeparator());
		this.add(saveMenuItem);
		this.add(saveAsMenuItem);
		this.add(saveAsImageMenuItem);
		this.add(new JSeparator());
		this.add(exitMenuItem);
	}

	@Override
	public void setAttributes() {
		Font menuFont = new Font("Dialog", Font.PLAIN, 12);

		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		setMenuItemFont(newMenuItem, menuFont);
		setMenuItemFont(openMenuItem, menuFont);
		setMenuItemFont(loadImageMenuItem, menuFont);
		setMenuItemFont(saveMenuItem, menuFont);
		setMenuItemFont(saveAsMenuItem, menuFont);
		setMenuItemFont(saveAsImageMenuItem, menuFont);
		setMenuItemFont(exitMenuItem, menuFont);
	}

	private void setMenuItemFont(JMenuItem item, Font font) {
		item.setFont(font);
	}

	@Override
	public void addEventHandler() {
		saveMenuItem.addActionListener(e -> {
			// 저장 기능 추가 예정
		});

		saveAsMenuItem.addActionListener(e -> {
			// 다른 이름으로 저장 기능 추가 예정
		});

		saveAsImageMenuItem.addActionListener(e -> {
			// 이미지로 저장 기능 추가 예정
		});

		loadImageMenuItem.addActionListener(e -> {
			// 이미지 로드 기능 추가 예정
		});

		openMenuItem.addActionListener(e -> {
			// 열기 기능 추가 예정
		});

		newMenuItem.addActionListener(e -> {
			// 새로 만들기 기능 추가 예정
		});

		exitMenuItem.addActionListener(e -> System.exit(0));
	}

	@Override
	public void initialize() {
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
}