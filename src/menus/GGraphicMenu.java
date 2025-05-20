package menus;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import frames.GComponent;
import frames.GDrawingPanel;

public class GGraphicMenu extends JMenu implements GComponent {
    private static final long serialVersionUID = 1L;

    private JMenu lineWidthMenu;
    private JMenu lineStyleMenu;
    private JMenu fontStyleMenu;
    private JMenu fontSizeMenu;

    private GDrawingPanel drawingPanel;

    public GGraphicMenu() {
        super("Graphic");
        this.createComponents();
        this.setAttributes();
        this.arrangeComponents();
        this.addEventHandler();
    }

    @Override
    public void createComponents() {
        lineWidthMenu = new JMenu("Line Width");
        lineStyleMenu = new JMenu("Line Style");
        fontStyleMenu = new JMenu("Font Style");
        fontSizeMenu = new JMenu("Font Size");

        JMenuItem thinLineItem = new JMenuItem("Thin");
        JMenuItem normalLineItem = new JMenuItem("Normal");
        JMenuItem thickLineItem = new JMenuItem("Thick");
        lineWidthMenu.add(thinLineItem);
        lineWidthMenu.add(normalLineItem);
        lineWidthMenu.add(thickLineItem);

        JMenuItem solidLineItem = new JMenuItem("Solid");
        JMenuItem dashedLineItem = new JMenuItem("Dashed");
        JMenuItem dottedLineItem = new JMenuItem("Dotted");
        lineStyleMenu.add(solidLineItem);
        lineStyleMenu.add(dashedLineItem);
        lineStyleMenu.add(dottedLineItem);

        JMenuItem plainFontItem = new JMenuItem("Plain");
        JMenuItem boldFontItem = new JMenuItem("Bold");
        JMenuItem italicFontItem = new JMenuItem("Italic");
        JMenuItem boldItalicFontItem = new JMenuItem("Bold Italic");
        fontStyleMenu.add(plainFontItem);
        fontStyleMenu.add(boldFontItem);
        fontStyleMenu.add(italicFontItem);
        fontStyleMenu.add(boldItalicFontItem);

        JMenuItem size8Item = new JMenuItem("8pt");
        JMenuItem size10Item = new JMenuItem("10pt");
        JMenuItem size12Item = new JMenuItem("12pt");
        JMenuItem size14Item = new JMenuItem("14pt");
        JMenuItem size16Item = new JMenuItem("16pt");
        JMenuItem size20Item = new JMenuItem("20pt");
        JMenuItem size24Item = new JMenuItem("24pt");
        JMenuItem size32Item = new JMenuItem("32pt");
        fontSizeMenu.add(size8Item);
        fontSizeMenu.add(size10Item);
        fontSizeMenu.add(size12Item);
        fontSizeMenu.add(size14Item);
        fontSizeMenu.add(size16Item);
        fontSizeMenu.add(size20Item);
        fontSizeMenu.add(size24Item);
        fontSizeMenu.add(size32Item);
    }

    @Override
    public void arrangeComponents() {
        this.add(lineWidthMenu);
        this.add(lineStyleMenu);
        this.add(new JSeparator());
        this.add(fontStyleMenu);
        this.add(fontSizeMenu);
    }

    @Override
    public void setAttributes() {
        Font menuFont = new Font("Dialog", Font.PLAIN, 12);

        lineWidthMenu.setFont(menuFont);
        lineStyleMenu.setFont(menuFont);
        fontStyleMenu.setFont(menuFont);
        fontSizeMenu.setFont(menuFont);

        for (int i = 0; i < lineWidthMenu.getItemCount(); i++)
            setMenuItemFont(lineWidthMenu.getItem(i), menuFont);
        for (int i = 0; i < lineStyleMenu.getItemCount(); i++)
            setMenuItemFont(lineStyleMenu.getItem(i), menuFont);
        for (int i = 0; i < fontStyleMenu.getItemCount(); i++)
            setMenuItemFont(fontStyleMenu.getItem(i), menuFont);
        for (int i = 0; i < fontSizeMenu.getItemCount(); i++)
            setMenuItemFont(fontSizeMenu.getItem(i), menuFont);
    }

    private void setMenuItemFont(JMenuItem item, Font font) {
        item.setFont(font);
    }

    @Override
    public void addEventHandler() {
        ActionListener dummyAction = e -> System.out.println("Selected menu: " + e.getActionCommand());

        for (int i = 0; i < lineWidthMenu.getItemCount(); i++)
            lineWidthMenu.getItem(i).addActionListener(dummyAction);
        for (int i = 0; i < lineStyleMenu.getItemCount(); i++)
            lineStyleMenu.getItem(i).addActionListener(dummyAction);
        for (int i = 0; i < fontStyleMenu.getItemCount(); i++)
            fontStyleMenu.getItem(i).addActionListener(dummyAction);
        for (int i = 0; i < fontSizeMenu.getItemCount(); i++)
            fontSizeMenu.getItem(i).addActionListener(dummyAction);
    }

    @Override
    public void initialize() {
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}