package menus;

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

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;

	public GFileMenu() {
		super("File");
		ActionHandler actionHandler = new ActionHandler();
		for(EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getName());
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eFileMenuItem.name());
			this.add(menuItem);
		}
	}

	public void newPanel(){
		System.out.println("new panel");
	}

	public void open(){
		System.out.println("open");
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
				Vector<GShape> shapes = (Vector<GShape>) in.readObject();
				this.drawingPanel.setShapes(shapes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void save(){
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".ged")) {
					file = new File(file.getPath() + ".ged");
				}
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				objectOutputStream.writeObject(this.drawingPanel.getShapes());
				objectOutputStream.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void saveAs(){
		System.out.println("saveAs");
	}

	public void quit(){
		System.out.println("quit");
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
