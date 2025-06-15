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
        super(GConstants.EMenuTexts.eEditMenu.getValue());
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
        updateSelectionState();
    }

    @Override
    public void arrangeComponents() {
        boolean firstItem = true;
        for (GConstants.EEditMenuItem eEditMenuItem : GConstants.EEditMenuItem.values()) {
            if (!firstItem && (eEditMenuItem == GConstants.EEditMenuItem.eCut ||
                    eEditMenuItem == GConstants.EEditMenuItem.eDuplicate ||
                    eEditMenuItem == GConstants.EEditMenuItem.eGroup ||
                    eEditMenuItem == GConstants.EEditMenuItem.eBringToFront ||
                    eEditMenuItem == GConstants.EEditMenuItem.eRotateRight)) {
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
    public void addEventHandler() {
        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                item.addActionListener(e -> {
                    String command = e.getActionCommand();
                    handleMenuAction(command);
                    updateUndoRedoState();
                    updateClipboardState();
                    updateGroupState();
                    updateLayerState();
                    updateTransformState();
                    updateSelectionState();
                });
            }
        }
    }

    @Override
    public void initialize() {
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        updateUndoRedoState();
        updateClipboardState();
        updateGroupState();
        updateLayerState();
        updateTransformState();
        updateSelectionState();
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
                case "duplicate":
                    duplicate();
                    break;
                case "selectAll":
                    selectAll();
                    break;
                case "deselectAll":
                    deselectAll();
                    break;
                case "group":
                    group();
                    break;
                case "unGroup":
                    unGroup();
                    break;
                case "bringToFront":
                    bringToFront();
                    break;
                case "sendToBack":
                    sendToBack();
                    break;
                case "bringForward":
                    bringForward();
                    break;
                case "sendBackward":
                    sendBackward();
                    break;
                case "rotateRight":
                    rotateRight();
                    break;
                case "rotateLeft":
                    rotateLeft();
                    break;
                case "flipHorizontal":
                    flipHorizontal();
                    break;
                case "flipVertical":
                    flipVertical();
                    break;
            }
        } catch (IllegalArgumentException e) {
        }
    }

    private void undo() {
        if (drawingPanel != null) {
            drawingPanel.undo();
        }
    }

    private void redo() {
        if (drawingPanel != null) {
            drawingPanel.redo();
        }
    }

    private void cut() {
        if (drawingPanel != null) {
            drawingPanel.cut();
        }
    }

    private void copy() {
        if (drawingPanel != null) {
            drawingPanel.copy();
        }
    }

    private void paste() {
        if (drawingPanel != null) {
            drawingPanel.paste();
        }
    }

    private void delete() {
        if (drawingPanel != null) {
            drawingPanel.delete();
        }
    }

    private void duplicate() {
        if (drawingPanel != null) {
            drawingPanel.duplicate();
        }
    }

    private void selectAll() {
        if (drawingPanel != null) {
            drawingPanel.selectAll();
        }
    }

    private void deselectAll() {
        if (drawingPanel != null) {
            drawingPanel.deselectAll();
        }
    }

    private void group() {
        if (drawingPanel != null) {
            drawingPanel.group();
        }
    }

    private void unGroup() {
        if (drawingPanel != null) {
            drawingPanel.ungroup();
        }
    }

    private void bringToFront() {
        if (drawingPanel != null) {
            drawingPanel.bringToFront();
        }
    }

    private void sendToBack() {
        if (drawingPanel != null) {
            drawingPanel.sendToBack();
        }
    }

    private void bringForward() {
        if (drawingPanel != null) {
            drawingPanel.bringForward();
        }
    }

    private void sendBackward() {
        if (drawingPanel != null) {
            drawingPanel.sendBackward();
        }
    }

    private void rotateRight() {
        if (drawingPanel != null) {
            drawingPanel.rotateRight();
        }
    }

    private void rotateLeft() {
        if (drawingPanel != null) {
            drawingPanel.rotateLeft();
        }
    }

    private void flipHorizontal() {
        if (drawingPanel != null) {
            drawingPanel.flipHorizontal();
        }
    }

    private void flipVertical() {
        if (drawingPanel != null) {
            drawingPanel.flipVertical();
        }
    }

    public void updateUndoRedoState() {
        if (drawingPanel == null) return;

        boolean canUndo = drawingPanel.canUndo();
        boolean canRedo = drawingPanel.canRedo();

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (GConstants.EEditMenuItem.eUndo.name().equals(command)) {
                    item.setEnabled(canUndo);
                } else if (GConstants.EEditMenuItem.eRedo.name().equals(command)) {
                    item.setEnabled(canRedo);
                }
            }
        }
    }

    public void updateClipboardState() {
        if (drawingPanel == null) return;

        boolean hasSelection = drawingPanel.hasSelection();
        boolean hasClipboardContent = drawingPanel.hasClipboardContent();

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (GConstants.EEditMenuItem.eCut.name().equals(command) ||
                        GConstants.EEditMenuItem.eCopy.name().equals(command) ||
                        GConstants.EEditMenuItem.eDelete.name().equals(command) ||
                        GConstants.EEditMenuItem.eDuplicate.name().equals(command)) {
                    item.setEnabled(hasSelection);
                } else if (GConstants.EEditMenuItem.ePaste.name().equals(command)) {
                    item.setEnabled(hasClipboardContent);
                }
            }
        }
    }

    public void updateGroupState() {
        if (drawingPanel == null) return;

        boolean canGroup = drawingPanel.canGroup();
        boolean canUngroup = drawingPanel.canUngroup();

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (GConstants.EEditMenuItem.eGroup.name().equals(command)) {
                    item.setEnabled(canGroup);
                } else if (GConstants.EEditMenuItem.eUnGroup.name().equals(command)) {
                    item.setEnabled(canUngroup);
                }
            }
        }
    }

    public void updateLayerState() {
        if (drawingPanel == null) return;

        boolean canBringToFront = drawingPanel.canBringToFront();
        boolean canSendToBack = drawingPanel.canSendToBack();
        boolean canBringForward = drawingPanel.canBringForward();
        boolean canSendBackward = drawingPanel.canSendBackward();

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (GConstants.EEditMenuItem.eBringToFront.name().equals(command)) {
                    item.setEnabled(canBringToFront);
                } else if (GConstants.EEditMenuItem.eSendToBack.name().equals(command)) {
                    item.setEnabled(canSendToBack);
                } else if (GConstants.EEditMenuItem.eBringForward.name().equals(command)) {
                    item.setEnabled(canBringForward);
                } else if (GConstants.EEditMenuItem.eSendBackward.name().equals(command)) {
                    item.setEnabled(canSendBackward);
                }
            }
        }
    }

    public void updateTransformState() {
        if (drawingPanel == null) return;

        boolean canTransform = drawingPanel.canTransform();

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (GConstants.EEditMenuItem.eRotateRight.name().equals(command) ||
                        GConstants.EEditMenuItem.eRotateLeft.name().equals(command) ||
                        GConstants.EEditMenuItem.eFlipHorizontal.name().equals(command) ||
                        GConstants.EEditMenuItem.eFlipVertical.name().equals(command)) {
                    item.setEnabled(canTransform);
                }
            }
        }
    }

    public void updateSelectionState() {
        if (drawingPanel == null) return;

        boolean hasShapes = drawingPanel.hasAnyShapes();
        boolean hasSelection = drawingPanel.hasSelection();

        for (int i = 0; i < this.getItemCount(); i++) {
            JMenuItem item = this.getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (GConstants.EEditMenuItem.eSelectAll.name().equals(command)) {
                    item.setEnabled(hasShapes);
                } else if (GConstants.EEditMenuItem.eDeselectAll.name().equals(command)) {
                    item.setEnabled(hasSelection);
                }
            }
        }
    }
}