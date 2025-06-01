package menus;

import frames.GComponent;
import frames.GDrawingPanel;
import global.GConstants;
import shapes.GShape;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

import static global.GConstants.*;

public class GFileMenu extends JMenu implements GComponent {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;
	private File currentFile;

	public GFileMenu() {
		super("File");
		this.currentFile = null;
		addEventHandler();
	}

	@Override
	public void createComponents() {
		//
	}

	@Override
	public void setAttributes() {
		//
	}

	@Override
	public void arrangeComponents() {
		//
	}

	@Override
	public void addEventHandler() {
		ActionHandler actionHandler = new ActionHandler();
		for(EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getName());
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eFileMenuItem.name());
			this.add(menuItem);
		}
	}

	public void newPanel(){
		this.currentFile = null;
		this.drawingPanel.setShapes(new Vector<GShape>());
		this.drawingPanel.repaint();
	}

	public void open(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
				Vector<GShape> shapes = (Vector<GShape>) in.readObject();
				this.drawingPanel.setShapes(shapes);
				this.currentFile = file;
				this.drawingPanel.repaint();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "파일을 열 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void save(){
		if (this.currentFile == null) {
			saveAs();
		} else {
			saveToFile(this.currentFile);
		}
	}

	public void saveAs(){
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
			if (this.currentFile != null) {
				chooser.setSelectedFile(this.currentFile);
			}
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".ged")) {
					file = new File(file.getPath() + ".ged");
				}
				if (saveToFile(file)) {
					this.currentFile = file;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "파일을 저장할 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean saveToFile(File file) {
		try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
			out.writeObject(this.drawingPanel.getShapes());
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "파일을 저장할 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public void quit(){
		System.exit(0);
	}

	public void print(){
		System.out.println("print");
	}

	private void invokeMethod(String name){
		try {
			this.getClass().getMethod(name).invoke(this);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialize(){
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFileMenuItem eFileMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
			invokeMethod(eFileMenuItem.getMethodName());
		}
	}
}