package components;

import shapes.GShape;
import states.GShapeState;
import utils.GTransFormer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GDrawingPanel extends JPanel implements GComponent {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private GTransFormer transFormer;
    private boolean isDrawingPolygon = false;
    private List<GShape> shapes = new ArrayList<>();
    private GShape previewShape;
    private BufferedImage bufferedImage;
    private Graphics2D bufferedGraphics;

    public GDrawingPanel() {
        setAttributes();
        addEventHandler();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bufferedImage == null) {
            int width = this.getWidth();
            int height = this.getHeight();
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            bufferedGraphics = bufferedImage.createGraphics();
            bufferedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, getWidth(), getHeight());

        for (GShape shape : shapes) {
            shape.draw(bufferedGraphics);
        }

        if (previewShape != null) {
            previewShape.draw(bufferedGraphics);
        }

        g.drawImage(bufferedImage, 0, 0, this);
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
        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    @Override
    public void initialize() {
        transFormer = new GTransFormer(this);
        previewShape = null;
    }

    public void setPreviewShape(GShape previewShape) {
        this.previewShape = previewShape;
        repaint();
    }

    public void setDrawingPolygon(boolean drawingPolygon) {
        isDrawingPolygon = drawingPolygon;
    }

    public void addShape(GShape shape) {
        this.shapes.add(shape);
        repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        bufferedImage = null;
    }

    public GTransFormer getTransFormer() {
        return transFormer;
    }

    public List<GShape> getShapes() {
        return shapes;
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (isDrawingPolygon && e.getClickCount() == 2) {
                GShapeState.getInstance().completePolygon();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            transFormer.start(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isDrawingPolygon) {
                transFormer.update(new Point(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            transFormer.finish(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (isDrawingPolygon) {
                transFormer.update(new Point(e.getX(), e.getY()));
            }
        }
    }
}