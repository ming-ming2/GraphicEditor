package menus;

import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import global.GConstants;
import frames.GComponent;
import frames.GDrawingPanel;

public class GEditMenu extends JMenu implements GComponent {
    private static final long serialVersionUID = 1L;

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
    }

    @Override
    public void arrangeComponents() {
        boolean firstItem = true;
        for (GConstants.EEditMenuItem eEditMenuItem : GConstants.EEditMenuItem.values()) {
            if (!firstItem && (eEditMenuItem == GConstants.EEditMenuItem.eCut ||
                    eEditMenuItem == GConstants.EEditMenuItem.eGroup)) {
                this.add(new JSeparator());
            }

            JMenuItem menuItem = new JMenuItem(eEditMenuItem.getName());
            menuItem.setActionCommand(eEditMenuItem.name());

            if (eEditMenuItem.getAccelerator() != null) {
                menuItem.setAccelerator(eEditMenuItem.getAccelerator());
            }
            if (eEditMenuItem.getToolTipText() != null && !eEditMenuItem.getToolTipText().isEmpty()) {
                menuItem.setToolTipText(eEditMenuItem.getToolTipText());
            }

            this.add(menuItem);
            firstItem = false;
        }
    }

    @Override
    public void setAttributes() {
        Font menuFont = new Font("Dialog", Font.PLAIN, 12);
        this.setFont(menuFont);

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                item.setFont(menuFont);
            }
        }

        updateUndoRedoState();
        updateClipboardState();
    }

    @Override
    public void addEventHandler() {
        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                item.addActionListener(e -> {
                    String command = e.getActionCommand();
                    handleMenuAction(command);
                });
            }
        }
    }

    private void handleMenuAction(String command) {
        try {
            GConstants.EEditMenuItem eEditMenuItem = GConstants.EEditMenuItem.valueOf(command);
            String methodName = eEditMenuItem.getMethodName();

            switch (methodName) {
                case "undo":
                    undo();
                    break;
                case "redo":
                    redo();
                    break;
                case "cut":
                    cut();
                    break;
                case "copy":
                    copy();
                    break;
                case "paste":
                    paste();
                    break;
                case "delete":
                    delete();
                    break;
                case "group":
                    group();
                    break;
                case "unGroup":
                    unGroup();
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown command: " + command);
        }
    }

    private void undo() {
        System.out.println("Undo - 구현 예정");
    }

    private void redo() {
        System.out.println("Redo - 구현 예정");
    }

    private void cut() {
        System.out.println("Cut - 구현 예정");
    }

    private void copy() {
        System.out.println("Copy - 구현 예정");
    }

    private void paste() {
        System.out.println("Paste - 구현 예정");
    }

    private void delete() {
        System.out.println("Delete - 구현 예정");
    }

    private void group() {
        System.out.println("Group - 구현 예정");
    }

    private void unGroup() {
        System.out.println("UnGroup - 구현 예정");
    }

    @Override
    public void initialize() {
    }

    public void updateUndoRedoState() {
        boolean canUndo = false;
        boolean canRedo = false;

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if ("eUndo".equals(command)) {
                    item.setEnabled(canUndo);
                } else if ("eRedo".equals(command)) {
                    item.setEnabled(canRedo);
                }
            }
        }
    }

    public void updateClipboardState() {
        boolean hasSelection = false;
        boolean hasClipboardContent = false;

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if ("eCut".equals(command) || "eCopy".equals(command)) {
                    item.setEnabled(hasSelection);
                } else if ("ePaste".equals(command)) {
                    item.setEnabled(hasClipboardContent);
                }
            }
        }
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}