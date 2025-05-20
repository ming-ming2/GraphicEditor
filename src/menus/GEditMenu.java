package menus;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import frames.GComponent;
import frames.GDrawingPanel;

public class GEditMenu extends JMenu implements GComponent {
    private static final long serialVersionUID = 1L;

    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;

    private GDrawingPanel drawingPanel;

    public GEditMenu() {
        super("Edit");
        this.createComponents();
        this.setAttributes();
        this.arrangeComponents();
        this.addEventHandler();
    }

    @Override
    public void createComponents() {
        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");
        cutMenuItem = new JMenuItem("Cut");
        copyMenuItem = new JMenuItem("Copy");
        pasteMenuItem = new JMenuItem("Paste");
    }

    @Override
    public void arrangeComponents() {
        this.add(undoMenuItem);
        this.add(redoMenuItem);
        this.add(new JSeparator());
        this.add(cutMenuItem);
        this.add(copyMenuItem);
        this.add(pasteMenuItem);
    }

    @Override
    public void setAttributes() {
        Font menuFont = new Font("Dialog", Font.PLAIN, 12);

        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        setMenuItemFont(undoMenuItem, menuFont);
        setMenuItemFont(redoMenuItem, menuFont);
        setMenuItemFont(cutMenuItem, menuFont);
        setMenuItemFont(copyMenuItem, menuFont);
        setMenuItemFont(pasteMenuItem, menuFont);

        updateUndoRedoState();
        updateClipboardState();
    }

    private void setMenuItemFont(JMenuItem item, Font font) {
        item.setFont(font);
    }

    @Override
    public void addEventHandler() {
        undoMenuItem.addActionListener(e -> {
            // Undo 기능 추가 예정
        });

        redoMenuItem.addActionListener(e -> {
            // Redo 기능 추가 예정
        });

        cutMenuItem.addActionListener(e -> {
            // Cut 기능 추가 예정
        });

        copyMenuItem.addActionListener(e -> {
            // Copy 기능 추가 예정
        });

        pasteMenuItem.addActionListener(e -> {
            // Paste 기능 추가 예정
        });
    }

    @Override
    public void initialize() {
    }

    public void updateUndoRedoState() {
        // 나중에 CommandManager와 연동
        boolean canUndo = false;
        boolean canRedo = false;
        undoMenuItem.setEnabled(canUndo);
        redoMenuItem.setEnabled(canRedo);
    }

    public void updateClipboardState() {
        // 나중에 Clipboard와 연동
        boolean hasSelection = false;
        boolean hasClipboardContent = false;
        cutMenuItem.setEnabled(hasSelection);
        copyMenuItem.setEnabled(hasSelection);
        pasteMenuItem.setEnabled(hasClipboardContent);
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}
