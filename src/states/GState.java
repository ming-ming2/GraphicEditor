package states;

import components.GDrawingPanel;

import java.awt.*;

public interface GState {
    void start(Point point);

    void update(Point point);

    void finish(Point point);

    void setDrawingPanel(GDrawingPanel drawingPanel);
}
