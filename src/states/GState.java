package states;

import commands.GCommandManager;
import components.GDrawingPanel;

import java.awt.*;

public abstract class GState {
    protected GDrawingPanel drawingPanel;
    protected GCommandManager commandManager;

    public abstract void start(Point point);

    public abstract void update(Point point);

    public abstract void finish(Point point);

    public void setDrawingPanel(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public void setCommandManager(GCommandManager commandManager) {
        this.commandManager = commandManager;
    }
}
