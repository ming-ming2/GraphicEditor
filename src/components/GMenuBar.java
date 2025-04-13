import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GMenuBar extends JMenuBar implements GComponent{
    private GFileMenu fileMenu;
    private GEditMenu editMenu;
    private GViewMenu viewMenu;
    private GGraphicMenu graphicMenu;

    private List<GComponent> menus = new ArrayList<>();

    public GMenuBar() {

    }

    @Override
    public void createComponents() {
        fileMenu = new GFileMenu();
        editMenu = new GEditMenu();
        viewMenu = new GViewMenu();
        graphicMenu = new GGraphicMenu();

        menus.add(fileMenu);
        menus.add(editMenu);
        menus.add(viewMenu);
        menus.add(graphicMenu);
    }

    @Override
    public void setAttributes() {
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        this.setBackground(new Color(245, 245, 245));

        Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);
        fileMenu.setFont(menuFont);
        editMenu.setFont(menuFont);
        viewMenu.setFont(menuFont);
        graphicMenu.setFont(menuFont);
    }

    @Override
    public void arrangeComponents() {
        add(fileMenu);
        add(editMenu);
        add(viewMenu);
        add(graphicMenu);
    }

    @Override
    public void addEventHandler() {

    }

    @Override
    public void initialize() {
        for (GComponent component : menus) {
            component.initialize();
        }
    }
}
