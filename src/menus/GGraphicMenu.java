package menus;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import frames.GComponent;
import frames.GDrawingPanel;
import global.GConstants;
import global.GGraphicsProperties;

public class GGraphicMenu extends JMenu implements GComponent {
    private static final long serialVersionUID = 1L;

    private JMenu lineWidthMenu;
    private JMenu lineStyleMenu;
    private JMenu fontStyleMenu;
    private JMenu fontSizeMenu;
    private JMenuItem lineColorItem;
    private JMenuItem fillColorItem;

    private GDrawingPanel drawingPanel;

    public GGraphicMenu() {
        super(GConstants.EMenuTexts.eGraphicMenu.getValue());
        this.createComponents();
        this.setAttributes();
        this.arrangeComponents();
        this.addEventHandler();
    }

    @Override
    public void createComponents() {
        lineWidthMenu = new JMenu(GConstants.EMenuTexts.eLineWidthMenu.getValue());
        lineStyleMenu = new JMenu(GConstants.EMenuTexts.eLineStyleMenu.getValue());
        fontStyleMenu = new JMenu(GConstants.EMenuTexts.eFontStyleMenu.getValue());
        fontSizeMenu = new JMenu(GConstants.EMenuTexts.eFontSizeMenu.getValue());
        lineColorItem = new JMenuItem(GConstants.EMenuTexts.eLineColorItem.getValue());
        fillColorItem = new JMenuItem(GConstants.EMenuTexts.eFillColorItem.getValue());

        JMenuItem thinLineItem = new JMenuItem(GConstants.EMenuTexts.eThinLine.getValue() + " (" + GConstants.EGraphicsDefaults.eThinLineWidth.getValue() + "px)");
        JMenuItem normalLineItem = new JMenuItem(GConstants.EMenuTexts.eNormalLine.getValue() + " (" + GConstants.EGraphicsDefaults.eNormalLineWidth.getValue() + "px)");
        JMenuItem thickLineItem = new JMenuItem(GConstants.EMenuTexts.eThickLine.getValue() + " (" + GConstants.EGraphicsDefaults.eThickLineWidth.getValue() + "px)");
        lineWidthMenu.add(thinLineItem);
        lineWidthMenu.add(normalLineItem);
        lineWidthMenu.add(thickLineItem);

        JMenuItem solidLineItem = new JMenuItem(GConstants.EMenuTexts.eSolidLine.getValue());
        JMenuItem dashedLineItem = new JMenuItem(GConstants.EMenuTexts.eDashedLine.getValue());
        JMenuItem dottedLineItem = new JMenuItem(GConstants.EMenuTexts.eDottedLine.getValue());
        lineStyleMenu.add(solidLineItem);
        lineStyleMenu.add(dashedLineItem);
        lineStyleMenu.add(dottedLineItem);

        JMenuItem plainFontItem = new JMenuItem(GConstants.EMenuTexts.ePlainFont.getValue());
        JMenuItem boldFontItem = new JMenuItem(GConstants.EMenuTexts.eBoldFont.getValue());
        JMenuItem italicFontItem = new JMenuItem(GConstants.EMenuTexts.eItalicFont.getValue());
        JMenuItem boldItalicFontItem = new JMenuItem(GConstants.EMenuTexts.eBoldItalicFont.getValue());
        fontStyleMenu.add(plainFontItem);
        fontStyleMenu.add(boldFontItem);
        fontStyleMenu.add(italicFontItem);
        fontStyleMenu.add(boldItalicFontItem);

        int[] fontSizes = GConstants.EFontSizes.getAllValues();
        for (int size : fontSizes) {
            JMenuItem sizeItem = new JMenuItem(size + "pt");
            fontSizeMenu.add(sizeItem);
        }
    }

    @Override
    public void arrangeComponents() {
        this.add(lineWidthMenu);
        this.add(lineStyleMenu);
        this.add(new JSeparator());
        this.add(lineColorItem);
        this.add(fillColorItem);
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
        lineColorItem.setFont(menuFont);
        fillColorItem.setFont(menuFont);

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
        for (int i = 0; i < lineWidthMenu.getItemCount(); i++) {
            JMenuItem item = lineWidthMenu.getItem(i);
            final int index = i;
            item.addActionListener(e -> {
                float[] widths = {
                        GConstants.EGraphicsDefaults.eThinLineWidth.getFloatValue(),
                        GConstants.EGraphicsDefaults.eNormalLineWidth.getFloatValue(),
                        GConstants.EGraphicsDefaults.eThickLineWidth.getFloatValue()
                };
                float selectedWidth = widths[index];

                GGraphicsProperties.getInstance().setLineWidth(selectedWidth);

                if (drawingPanel != null) {
                    drawingPanel.applyLineWidthToSelected(selectedWidth);
                }

                System.out.println("Line width set to: " + selectedWidth + "px");
            });
        }

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

                GGraphicsProperties.getInstance().setLineStyle(selectedStyle);

                if (drawingPanel != null) {
                    drawingPanel.applyLineStyleToSelected(selectedStyle);
                }

                System.out.println("Line style set to: " + selectedStyle);
            });
        }

        lineColorItem.addActionListener(e -> {
            Color currentColor = GGraphicsProperties.getInstance().getLineColor();
            Color selectedColor = JColorChooser.showDialog(this, GConstants.EMenuTexts.eSelectLineColor.getValue(), currentColor);
            if (selectedColor != null) {
                GGraphicsProperties.getInstance().setLineColor(selectedColor);

                if (drawingPanel != null) {
                    drawingPanel.applyLineColorToSelected(selectedColor);
                }

                System.out.println("Line color set to: " + selectedColor);
            }
        });

        fillColorItem.addActionListener(e -> {
            Color currentColor = GGraphicsProperties.getInstance().getFillColor();
            Color selectedColor = JColorChooser.showDialog(this, GConstants.EMenuTexts.eSelectFillColor.getValue(), currentColor);
            if (selectedColor != null) {
                GGraphicsProperties.getInstance().setFillColor(selectedColor);

                if (drawingPanel != null) {
                    drawingPanel.applyFillColorToSelected(selectedColor);
                }

                System.out.println("Fill color set to: " + selectedColor);
            }
        });

        for (int i = 0; i < fontStyleMenu.getItemCount(); i++) {
            JMenuItem item = fontStyleMenu.getItem(i);
            final int index = i;
            item.addActionListener(e -> {
                int[] styles = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};
                int selectedStyle = styles[index];

                GGraphicsProperties.getInstance().setFontStyle(selectedStyle);

                if (drawingPanel != null) {
                    drawingPanel.applyFontStyleToSelected(selectedStyle);
                }

                System.out.println("Font style set to: " + selectedStyle);
            });
        }

        for (int i = 0; i < fontSizeMenu.getItemCount(); i++) {
            JMenuItem item = fontSizeMenu.getItem(i);
            final int index = i;
            item.addActionListener(e -> {
                int[] sizes = GConstants.EFontSizes.getAllValues();
                int selectedSize = sizes[index];

                GGraphicsProperties.getInstance().setFontSize(selectedSize);

                if (drawingPanel != null) {
                    drawingPanel.applyFontSizeToSelected(selectedSize);
                }

                System.out.println("Font size set to: " + selectedSize + "pt");
            });
        }
    }

    @Override
    public void initialize() {
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}