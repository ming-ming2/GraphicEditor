package menus;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import frames.GComponent;
import frames.GDrawingPanel;

public class GViewMenu extends JMenu implements GComponent {
    private static final long serialVersionUID = 1L;

    private JMenuItem zoomInMenuItem;
    private JMenuItem zoomOutMenuItem;
    private JMenuItem zoomResetMenuItem;

    private GDrawingPanel drawingPanel;

    public GViewMenu() {
        super("View");
        this.createComponents();
        this.setAttributes();
        this.arrangeComponents();
        this.addEventHandler();
    }

    @Override
    public void createComponents() {
        zoomInMenuItem = new JMenuItem("Zoom In");
        zoomOutMenuItem = new JMenuItem("Zoom Out");
        zoomResetMenuItem = new JMenuItem("Reset Zoom (100%)");
    }

    @Override
    public void arrangeComponents() {
        this.add(zoomInMenuItem);
        this.add(zoomOutMenuItem);
        this.add(zoomResetMenuItem);
    }

    @Override
    public void setAttributes() {
        Font menuFont = new Font("Dialog", Font.PLAIN, 12);

        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        zoomResetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));

        setMenuItemFont(zoomInMenuItem, menuFont);
        setMenuItemFont(zoomOutMenuItem, menuFont);
        setMenuItemFont(zoomResetMenuItem, menuFont);
    }

    private void setMenuItemFont(JMenuItem item, Font font) {
        item.setFont(font);
    }

    @Override
    public void addEventHandler() {
        zoomInMenuItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.zoomIn();
            }
        });

        zoomOutMenuItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.zoomOut();
            }
        });

        zoomResetMenuItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.resetZoom();
            }
        });
    }

    @Override
    public void initialize() {
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}