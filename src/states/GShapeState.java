package states;

import commands.GCommandManager;
import commands.GShapeCommand;
import components.GDrawingPanel;
import shapes.GPolygon;
import shapes.GShape;
import shapes.GShapeFactory;
import types.GShapeType;

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
        return instance;
    }

    public void setShapeType(GShapeType shapeType) {
        this.shapeType = shapeType;
    }

    @Override
    public void start(Point point) {
        if (shapeType == GShapeType.POLYGON && shape instanceof GPolygon) {
            ((GPolygon) shape).addPoint(point);
        } else {
            shape = GShapeFactory.createShape(shapeType);
            if (shape != null) {
                if (shape instanceof GPolygon) {
                    ((GPolygon) shape).addPoint(point);
                } else {
                    shape.setStart(point);
                    shape.setEnd(point);
                }
            }
        }
        drawingPanel.setPreviewShape(shape);
        drawingPanel.setDrawingPolygon(shapeType == GShapeType.POLYGON);
    }

    @Override
    public void update(Point point) {
        if (shape != null) {
            shape.resize(point);
            drawingPanel.setPreviewShape(shape);
        }
    }

    @Override
    public void finish(Point point) {
        if (shapeType == GShapeType.POLYGON) {
            ((GPolygon) shape).addPoint(point);
            drawingPanel.setPreviewShape(shape);
        } else {
            GShapeCommand command = new GShapeCommand(drawingPanel);
            command.setShape(shape);
            commandManager.execute(command);
            shape = null;
            drawingPanel.setPreviewShape(null);
        }
    }

    public void completePolygon() {
        if (shapeType == GShapeType.POLYGON && shape instanceof GPolygon && ((GPolygon) shape).getPoints().size() >= 3) {
            ((GPolygon) shape).complete();
            GShapeCommand command = new GShapeCommand(drawingPanel);
            command.setShape(shape);
            commandManager.execute(command);
            shape = null;
            drawingPanel.setPreviewShape(null);
            drawingPanel.setDrawingPolygon(false);
        }
    }
}