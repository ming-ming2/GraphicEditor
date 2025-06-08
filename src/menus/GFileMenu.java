package menus;

import frames.GComponent;
import frames.GDrawingPanel;
import shapes.GShape;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import static global.GConstants.*;

public class GFileMenu extends JMenu implements GComponent {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;
	private File file;
	private File dir;

	public GFileMenu() {
		super("File");
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
		if(this.close()) {
			this.file = null;
			this.drawingPanel.initialize();
		}
	}

	public void open(){
		if(this.drawingPanel.isUpdated()){
			this.save();
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(selectedFile))) {
				this.drawingPanel.setShapes((Vector<GShape>) in.readObject());
				this.file = selectedFile;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "파일을 열 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void save(){
		if (this.file == null) {
			saveAs();
		} else {
			saveToFile(this.file);
		}
	}

	public void saveAs(){
		try {
			JFileChooser chooser = new JFileChooser(this.dir);
			chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
			if (this.file != null) {
				chooser.setSelectedFile(this.file);
			}
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				this.dir = chooser.getCurrentDirectory();
				if (!selectedFile.getName().toLowerCase().endsWith(".ged")) {
					selectedFile = new File(selectedFile.getPath() + ".ged");
				}
				if (saveToFile(selectedFile)) {
					this.file = selectedFile;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "파일을 저장할 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean saveToFile(File file) {
		try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)))) {
			out.writeObject(this.drawingPanel.getShapes());
			this.drawingPanel.setBUpdated(false);
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "파일을 저장할 수 없습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public boolean close(){
		boolean bCancel = false;
		if(this.drawingPanel.isUpdated()) {
			int reply = JOptionPane.NO_OPTION;
			reply = JOptionPane.showConfirmDialog(this.drawingPanel,"변경 내용을 저장할까요?");
			if(reply == JOptionPane.CANCEL_OPTION) {
				bCancel = true;
			}
			if(reply == JOptionPane.YES_OPTION) {
				this.save();
			} else if(reply == JOptionPane.NO_OPTION) {

			} else if(reply == JOptionPane.CANCEL_OPTION) {
				bCancel = true;
			}
		} else {
			this.quit();
		}
		return !bCancel;
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
		this.dir = new File("/Users/abcd/IdeaProjects/GraphicEditor_project");
		this.file = null;
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