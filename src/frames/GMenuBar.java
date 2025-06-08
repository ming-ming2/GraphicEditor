package frames;

import menus.GEditMenu;
import menus.GFileMenu;
import menus.GGraphicMenu;
import menus.GViewMenu;

import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar implements GComponent {
	private static final long serialVersionUID = 1L;

	private GFileMenu fileMenu;
	private GEditMenu editMenu;
	private GViewMenu viewMenu;
	private GGraphicMenu graphicMenu;

	private GDrawingPanel drawingPanel;
	private List<GComponent> menus = new ArrayList<>();

	public GMenuBar() {
		this.createComponents();
		this.setAttributes();
		this.arrangeComponents();
		this.addEventHandler();
	}

	@Override
	public void createComponents() {
		fileMenu = new GFileMenu();
		editMenu = new GEditMenu();
		viewMenu = new GViewMenu();
		graphicMenu = new GGraphicMenu();

		menus.add(fileMenu);
		menus.add(editMenu);
		menus.add(viewMenu);
		menus.add(graphicMenu);
	}

	@Override
	public void arrangeComponents() {
		this.add(fileMenu);
		this.add(editMenu);
		this.add(viewMenu);
		this.add(graphicMenu);
	}

	@Override
	public void setAttributes() {
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
		this.setBackground(new Color(245, 245, 245));

		Font menuFont = new Font("Dialog", Font.PLAIN, 12);
		fileMenu.setFont(menuFont);
		editMenu.setFont(menuFont);
		viewMenu.setFont(menuFont);
		graphicMenu.setFont(menuFont);
	}

	@Override
	public void addEventHandler() {
	}

	@Override
	public void initialize() {
		for(GComponent menu : menus) {
			menu.initialize();
		}
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		fileMenu.associate(drawingPanel);
		editMenu.associate(drawingPanel);
		viewMenu.associate(drawingPanel);
		graphicMenu.associate(drawingPanel);
	}

	public GFileMenu getFileMenu() {
		return fileMenu;
	}
}