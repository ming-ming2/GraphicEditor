package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newItem;
	
	public GFileMenu() {
		super("File");
		newItem = new JMenuItem("new");
		this.add(newItem);
	}

}
