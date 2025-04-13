package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GToolBar extends JToolBar implements GComponent {
    private JButton currentSelectedButton;
    private Color selectedColor;
    private List<JButton> shapeButtons = new ArrayList<>();

    private JPanel shapesSection;


    public GToolBar() {
        createComponents();
        setAttributes();
        arrangeComponents();
        addEventHandler();
    }

    @Override
    public void createComponents() {
        shapesSection = new JPanel();
        shapesSection.setLayout(new BoxLayout(shapesSection, BoxLayout.X_AXIS));
        shapesSection.setOpaque(false);

        JLabel sectionLabel = new JLabel("도형: ");
        shapesSection.add(sectionLabel);
        shapesSection.add(Box.createHorizontalStrut(getSmallSpacing()));

        JPanel shapesPanel = new JPanel();
        shapesPanel.setLayout(new GridLayout(1, 2, getSmallSpacing(), getSmallSpacing()));
        shapesPanel.setBackground(Color.WHITE);

        // 직사각형과 다각형 버튼만 추가
        addShapeButton(shapesPanel, "직사각형", "Rectangle");
        addShapeButton(shapesPanel, "다각형", "Polygon");

        shapesSection.add(shapesPanel);
    }

    @Override
    public void setAttributes() {
        this.setFloatable(false);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        this.setPreferredSize(new Dimension(0, getToolbarHeight()));
        this.setBackground(new Color(240, 240, 240));
        this.selectedColor = new Color(230, 230, 250);
    }

    @Override
    public void arrangeComponents() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(shapesSection);
    }

    @Override
    public void addEventHandler() {

    }

    @Override
    public void initialize() {
        this.currentSelectedButton = null;
    }

    private JButton addShapeButton(JPanel panel, String tooltip, String shapeType) {
        JButton button = new JButton();
        button.setToolTipText(tooltip);

        Dimension buttonSize = new Dimension(getButtonSize(), getButtonSize());
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);

        // 아이콘 설정
        if (shapeType.equals("Rectangle")) {
            button.setIcon(createRectangleIcon());
        } else if (shapeType.equals("Polygon")) {
            button.setIcon(createPolygonIcon());
        }

        // 마우스 이벤트 (호버 효과)
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (currentSelectedButton != button) {
                    button.setBackground(new Color(245, 245, 255));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (currentSelectedButton != button) {
                    button.setBackground(Color.WHITE);
                }
            }
        });

        panel.add(button);
        shapeButtons.add(button);
        return button;
    }

    private Icon createRectangleIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1.0f));

                int iconSize = getIconSize();
                int padding = (getButtonSize() - iconSize) / 2;

                int width = iconSize;
                int height = iconSize;

                int offsetX = x + padding;
                int offsetY = y + padding;

                g2d.drawRect(offsetX, offsetY, width, height);
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return getButtonSize();
            }

            @Override
            public int getIconHeight() {
                return getButtonSize();
            }
        };
    }

    private Icon createPolygonIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1.0f));

                int iconSize = getIconSize();
                int padding = (getButtonSize() - iconSize) / 2;

                int width = iconSize;
                int height = iconSize;

                int offsetX = x + padding;
                int offsetY = y + padding;

                // 다각형 그리기 (5각형으로 표현)
                int[] xPoints = new int[5];
                int[] yPoints = new int[5];

                int centerX = offsetX + width / 2;
                int centerY = offsetY + height / 2;
                int radius = Math.min(width, height) / 2;

                for (int i = 0; i < 5; i++) {
                    double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
                    xPoints[i] = (int) (centerX + radius * Math.cos(angle));
                    yPoints[i] = (int) (centerY + radius * Math.sin(angle));
                }

                g2d.drawPolygon(xPoints, yPoints, 5);

                // 점찍기 효과를 위해 꼭지점에 작은 점 표시
                for (int i = 0; i < 5; i++) {
                    g2d.fillOval(xPoints[i] - 2, yPoints[i] - 2, 4, 4);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return getButtonSize();
            }

            @Override
            public int getIconHeight() {
                return getButtonSize();
            }
        };
    }

    // 유틸리티 메서드
    private int getToolbarHeight() {
        return 40;  // 툴바 높이 
    }

    private int getButtonSize() {
        return 30;  // 버튼 크기
    }

    private int getIconSize() {
        return 20;  // 아이콘 크기
    }

    private int getSmallSpacing() {
        return 5;   // 작은 간격
    }
}