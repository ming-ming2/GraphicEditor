package utils;

import components.GDrawingPanel;
import states.GDefaultState;
import states.GState;

import java.awt.*;

public class GTransFormer {
    private GState currentState;
    private GDrawingPanel drawingPanel;

    public GTransFormer(GDrawingPanel drawingPanel) {
        this.currentState = new GDefaultState();
        this.drawingPanel = drawingPanel;
        this.currentState.setDrawingPanel(drawingPanel);
    }

    public void setState(GState drawingState) {
        this.currentState = drawingState;
        this.currentState.setDrawingPanel(this.drawingPanel);
    }

    public void start(Point point) {
        currentState.start(point);
    }

    public void update(Point point) {
        currentState.update(point);
    }

    public void finish(Point point) {
        currentState.finish(point);
    }

    public GState getCurrentState() {
        return currentState;
    }
}