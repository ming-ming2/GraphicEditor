package components;

import shapes.GShape;
import utils.GTransFormer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class GDrawingPanel extends JPanel implements GComponent {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private GTransFormer transFormer;
    private boolean isDrawingPolygon = false;
    private Graphics2D g;
    private List<GShape> shapes = new ArrayList<>();
    private GShape previewShape;

    public GDrawingPanel() {
        setAttributes();
        addEventHandler();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GShape shape : shapes) {
            shape.draw((Graphics2D) g);
        }

    }

    @Override
    public void createComponents() {

    }

    @Override
    public void setAttributes() {
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setBorder(new LineBorder(Color.LIGHT_GRAY));
    }

    @Override
    public void arrangeComponents() {

    }

    @Override
    public void addEventHandler() {
        this.addMouseListener(new MouseHandler());
    }

    @Override
    public void initialize() {
        transFormer = new GTransFormer(this);
        previewShape = null;
    }

    public void setPreviewShape(GShape previewShape) {
        this.previewShape = previewShape;
    }

    public void setDrawingPolygon(boolean drawingPolygon) {
        isDrawingPolygon = drawingPolygon;
    }

    public void addShape(GShape shape) {
        this.shapes.add(shape);
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {
        private Graphics2D g = (Graphics2D) getGraphics();

        @Override
        public void mouseClicked(MouseEvent e) {
            //
        }

        @Override
        public void mousePressed(MouseEvent e) {
            transFormer.start(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isDrawingPolygon) transFormer.update(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            transFormer.finish(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (isDrawingPolygon) transFormer.update(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
