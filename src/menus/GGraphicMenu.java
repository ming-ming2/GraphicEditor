package menus;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import frames.GComponent;
import frames.GDrawingPanel;
import global.GGraphicsProperties;

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

        JMenuItem thinLineItem = new JMenuItem("Thin (1px)");
        JMenuItem normalLineItem = new JMenuItem("Normal (2px)");
        JMenuItem thickLineItem = new JMenuItem("Thick (4px)");
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
        // Line Width handlers
        for (int i = 0; i < lineWidthMenu.getItemCount(); i++) {
            JMenuItem item = lineWidthMenu.getItem(i);
            final int index = i;
            item.addActionListener(e -> {
                float[] widths = {1.0f, 2.0f, 4.0f};
                float selectedWidth = widths[index];

                // 전역 설정 변경 (새로 그릴 도형용)
                GGraphicsProperties.getInstance().setLineWidth(selectedWidth);

                // 선택된 도형들에 적용
                if (drawingPanel != null) {
                    drawingPanel.applyLineWidthToSelected(selectedWidth);
                }

                System.out.println("Line width set to: " + selectedWidth + "px");
            });
        }

        // Line Style handlers
        for (int i = 0; i < lineStyleMenu.getItemCount(); i++) {
            JMenuItem item = lineStyleMenu.getItem(i);
            final int index = i;
            item.addActionListener(e -> {
                GGraphicsProperties.LineStyle[] styles = {
                        GGraphicsProperties.LineStyle.SOLID,
                        GGraphicsProperties.LineStyle.DASHED,
                        GGraphicsProperties.LineStyle.DOTTED
                };
                GGraphicsProperties.LineStyle selectedStyle = styles[index];

                // 전역 설정 변경 (새로 그릴 도형용)
                GGraphicsProperties.getInstance().setLineStyle(selectedStyle);

                // 선택된 도형들에 적용
                if (drawingPanel != null) {
                    drawingPanel.applyLineStyleToSelected(selectedStyle);
                }

                System.out.println("Line style set to: " + selectedStyle);
            });
        }

        // Font Style handlers (placeholder)
        for (int i = 0; i < fontStyleMenu.getItemCount(); i++) {
            fontStyleMenu.getItem(i).addActionListener(e ->
                    System.out.println("Font style: " + e.getActionCommand()));
        }

        // Font Size handlers (placeholder)
        for (int i = 0; i < fontSizeMenu.getItemCount(); i++) {
            fontSizeMenu.getItem(i).addActionListener(e ->
                    System.out.println("Font size: " + e.getActionCommand()));
        }
    }

    @Override
    public void initialize() {
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}