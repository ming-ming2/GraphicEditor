import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GMainFrame extends JFrame implements GComponent {
    private GDrawingPanel drawingPanel;
    private GMenuBar menuBar;
    private GToolBar toolBar;
    private List<GComponent> componets = new ArrayList<GComponent>();

    public GMainFrame() {
        setAttributes();
        createComponents();
        arrangeComponents();
        addEventHandler();
    }


    @Override
    public void createComponents() {
        drawingPanel = new GDrawingPanel();
        menuBar = new GMenuBar();
        toolBar = new GToolBar();

        componets.add(drawingPanel);
        componets.add(menuBar);
        componets.add(toolBar);
    }

    @Override
    public void setAttributes() {
        this.setVisible(true);
        this.setTitle("Graphic Editor_V1");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (dim.width / 2.), dim.height * 2 / 3);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void arrangeComponents() {
        this.setLayout(new BorderLayout());
        this.setJMenuBar(menuBar);
        this.add(toolBar, BorderLayout.NORTH);
    }

    @Override
    public void addEventHandler() {

    }

    public void initialize() {
        for (GComponent component : componets) {
            component.initialize();
        }
    }
}
