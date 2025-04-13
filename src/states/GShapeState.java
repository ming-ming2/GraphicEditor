package states;

import commands.GCommandManager;
import commands.GShapeCommand;
import components.GDrawingPanel;
import shapes.GShape;
import shapes.GShapeFactory;
import types.GShapeType;
import types.GStateType;

import java.awt.*;

public class GShapeState extends GState {
    private static GShapeState instance;

    private GShapeType shapeType;
    private GShape shape;

    private GShapeState() {
    }

    public static GShapeState getInstance() {
        if (instance == null) {
            instance = new GShapeState();
        }
        return new GShapeState();
    }


    public void setShapeType(GShapeType shapeType) {
        this.shapeType = shapeType;
    }

    @Override
    public void start(Point point) {
        shape = GShapeFactory.createShape(shapeType);
        if (shape != null) {
            shape.setStart(point);
            shape.setEnd(point);
        }
    }

    @Override
    public void update(Point point) {
        shape.setEnd(point);
        drawingPanel.setPreviewShape(shape);
    }

    @Override
    public void finish(Point point) {
        GShapeCommand command = new GShapeCommand(drawingPanel);
        command.setShape(shape);
        commandManager.execute(command);
    }
}
