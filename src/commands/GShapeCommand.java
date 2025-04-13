package commands;

import components.GDrawingPanel;
import shapes.GShape;
import shapes.GShapeFactory;
import types.GShapeType;

public class GShapeCommand implements GCommand {
    private GDrawingPanel drawingPanel;
    private GShape shape;


    public GShapeCommand(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    @Override
    public void execute() {
        drawingPanel.addShape(shape);
        System.out.println("도형 커맨드 실행!");
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    public void setShape(GShape shape) {
        this.shape = shape;
    }
}
