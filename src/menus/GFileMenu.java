package menus;

import frames.GComponent;
import frames.GDrawingPanel;
import shapes.GShape;
import global.GConstants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

public class GFileMenu extends JMenu implements GComponent {
	private static final long serialVersionUID = 1L;
	private GDrawingPanel drawingPanel;
	private File file;
	private File dir;

	public GFileMenu() {
		super(GConstants.EMenuTexts.eFileMenu.getValue());
		this.createComponents();
		this.setAttributes();
		this.arrangeComponents();
		this.addEventHandler();
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
		ActionHandler actionHandler = new ActionHandler();
		for(GConstants.EFileMenuItem eFileMenuItem : GConstants.EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getName());
			menuItem.addActionListener(actionHandler);
			menuItem.setActionCommand(eFileMenuItem.name());

			if (eFileMenuItem.getAccelerator() != null) {
				menuItem.setAccelerator(eFileMenuItem.getAccelerator());
			}
			if (eFileMenuItem.getToolTipText() != null && !eFileMenuItem.getToolTipText().isEmpty()) {
				menuItem.setToolTipText(eFileMenuItem.getToolTipText());
			}

			this.add(menuItem);
		}
	}

	public void newPanel(){
		if(this.checkAndSave()) {
			this.file = null;
			this.drawingPanel.initialize();
		}
	}

	private boolean checkAndSave() {
		if(this.drawingPanel.isUpdated()) {
			int reply = JOptionPane.showConfirmDialog(this.drawingPanel, GConstants.EDialogTexts.eSaveConfirm.getValue());
			if(reply == JOptionPane.YES_OPTION) {
				this.save();
				return true;
			} else if(reply == JOptionPane.NO_OPTION) {
				return true;
			} else if(reply == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return true;
	}

	public void open(){
		if(this.drawingPanel.isUpdated()){
			this.save();
		}
		JFileChooser chooser = new JFileChooser(this.dir);
		chooser.setFileFilter(new FileNameExtensionFilter("Graphic Editor Files (*.ged)", "ged"));
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(selectedFile))) {
				this.drawingPanel.setShapes((Vector<GShape>) in.readObject());
				this.file = selectedFile;
				this.drawingPanel.setBUpdated(false);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, GConstants.EDialogTexts.eFileOpenError.getValue() + e.getMessage(), GConstants.EDialogTexts.eErrorTitle.getValue(), JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, GConstants.EDialogTexts.eFileSaveError.getValue() + e.getMessage(), GConstants.EDialogTexts.eErrorTitle.getValue(), JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean saveToFile(File file) {
		try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
			out.writeObject(this.drawingPanel.getShapes());
			this.drawingPanel.setBUpdated(false);
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, GConstants.EDialogTexts.eFileSaveError.getValue() + e.getMessage(), GConstants.EDialogTexts.eErrorTitle.getValue(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public boolean close(){
		boolean bCancel = false;
		if(this.drawingPanel.isUpdated()) {
			int reply = JOptionPane.showConfirmDialog(this.drawingPanel, GConstants.EDialogTexts.eSaveConfirm.getValue());
			if(reply == JOptionPane.YES_OPTION) {
				this.save();
			} else if(reply == JOptionPane.NO_OPTION) {
			} else if(reply == JOptionPane.CANCEL_OPTION) {
				bCancel = true;
			}
		}

		if(!bCancel) {
			this.quit();
		}
		return !bCancel;
	}

	public void quit(){
		System.exit(0);
	}

	public void print(){
		if (this.drawingPanel != null) {
			this.drawingPanel.printPanel();
		}
	}

	private void invokeMethod(String name){
		try {
			this.getClass().getMethod(name).invoke(this);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(){
		this.dir = new File(GConstants.EFileMenuItem.getDefaultPathName());
		this.file = null;
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			GConstants.EFileMenuItem eFileMenuItem = GConstants.EFileMenuItem.valueOf(e.getActionCommand());
			invokeMethod(eFileMenuItem.getMethodName());
		}
	}
}