package shapes;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public class GTextBox extends GShape {
    private Rectangle2D rectangle;
    private String text;
    private Font font;
    private boolean isEditing;
    private int cursorPosition;
    private long lastBlinkTime;
    private boolean cursorVisible;
    private List<String> wrappedLines;
    private int cursorLine;
    private int cursorColumn;
    private int startX, startY;

    private static final int CURSOR_BLINK_INTERVAL = 500;
    private static final int PADDING = 5;

    public GTextBox() {
        super(new Rectangle2D.Float(0, 0, 0, 0));
        this.rectangle = (Rectangle2D) this.getShape();
        this.text = "";
        this.font = new Font("Arial", Font.PLAIN, 14);
        this.isEditing = false;
        this.cursorPosition = 0;
        this.lastBlinkTime = System.currentTimeMillis();
        this.cursorVisible = true;
        this.wrappedLines = new ArrayList<>();
        this.cursorLine = 0;
        this.cursorColumn = 0;
        updateWrappedLines();
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.rectangle.setFrame(x, y, 0, 0);
    }

    @Override
    public void dragPoint(int x, int y) {
        double minX = Math.min(startX, x);
        double minY = Math.min(startY, y);
        double width = Math.abs(x - startX);
        double height = Math.abs(y - startY);
        this.rectangle.setFrame(minX, minY, width, height);
        updateWrappedLines();
        updateCursorPosition();
    }

    @Override
    public void addPoint(int x, int y) {
    }

    public void startEditing() {
        this.isEditing = true;
        this.cursorPosition = text.length();
        updateCursorPosition();
        this.lastBlinkTime = System.currentTimeMillis();
        this.cursorVisible = true;
    }

    public void stopEditing() {
        this.isEditing = false;
        this.cursorVisible = false;
    }

    public boolean isEditing() {
        return this.isEditing;
    }

    public void insertText(String newText) {
        if (!isEditing) return;

        StringBuilder sb = new StringBuilder(text);
        sb.insert(cursorPosition, newText);
        this.text = sb.toString();
        this.cursorPosition += newText.length();
        updateWrappedLines();
        updateCursorPosition();
    }

    public void deleteChar() {
        if (!isEditing || cursorPosition <= 0) return;

        StringBuilder sb = new StringBuilder(text);
        sb.deleteCharAt(cursorPosition - 1);
        this.text = sb.toString();
        this.cursorPosition--;
        updateWrappedLines();
        updateCursorPosition();
    }

    public void insertNewLine() {
        insertText("\n");
    }

    public void moveCursor(int direction) {
        if (!isEditing) return;

        if (direction > 0 && cursorPosition < text.length()) {
            cursorPosition++;
        } else if (direction < 0 && cursorPosition > 0) {
            cursorPosition--;
        }
        updateCursorPosition();
    }

    private void updateWrappedLines() {
        wrappedLines.clear();

        if (text.isEmpty()) {
            wrappedLines.add("");
            return;
        }

        AffineTransform currentTransform = this.getAffineTransform();
        double[] matrix = new double[6];
        currentTransform.getMatrix(matrix);
        double scaleX_mag = Math.sqrt(matrix[0] * matrix[0] + matrix[1] * matrix[1]);

        double boxWidth = rectangle.getWidth() * scaleX_mag - 2 * PADDING;

        if (boxWidth <= 0) {
            wrappedLines.add(text);
            return;
        }

        String[] paragraphs = text.split("\n", -1);

        FontRenderContext frc = new FontRenderContext(null, true, true);

        for (String paragraph : paragraphs) {
            if (paragraph.isEmpty()) {
                wrappedLines.add("");
                continue;
            }

            AttributedString attributedString = new AttributedString(paragraph);
            attributedString.addAttribute(TextAttribute.FONT, font);

            AttributedCharacterIterator iterator = attributedString.getIterator();
            LineBreakMeasurer measurer = new LineBreakMeasurer(iterator, frc);

            while (measurer.getPosition() < paragraph.length()) {
                TextLayout layout = measurer.nextLayout((float) boxWidth);
                wrappedLines.add(paragraph.substring(measurer.getPosition() - layout.getCharacterCount(), measurer.getPosition()));
            }
        }
    }

    private void updateCursorPosition() {
        cursorLine = 0;
        cursorColumn = 0;

        if (text.isEmpty()) {
            return;
        }

        AffineTransform currentTransform = this.getAffineTransform();
        double[] matrix = new double[6];
        currentTransform.getMatrix(matrix);
        double scaleX_mag = Math.sqrt(matrix[0] * matrix[0] + matrix[1] * matrix[1]);

        double boxWidth = rectangle.getWidth() * scaleX_mag - 2 * PADDING;

        int charCount = 0;
        String[] paragraphs = text.split("\n", -1);

        FontRenderContext frc = new FontRenderContext(null, true, true);

        for (int p = 0; p < paragraphs.length; p++) {
            String paragraph = paragraphs[p];

            if (cursorPosition <= charCount + paragraph.length()) {
                int posInParagraph = cursorPosition - charCount;

                if (paragraph.isEmpty()) {
                    cursorColumn = 0;
                    return;
                }

                if (boxWidth <= 0) {
                    cursorColumn = posInParagraph;
                    return;
                }

                AttributedString attributedString = new AttributedString(paragraph);
                attributedString.addAttribute(TextAttribute.FONT, font);

                AttributedCharacterIterator iterator = attributedString.getIterator();
                LineBreakMeasurer measurer = new LineBreakMeasurer(iterator, frc);

                int lineStartInParagraph = 0;
                while (measurer.getPosition() < paragraph.length()) {
                    TextLayout layout = measurer.nextLayout((float) boxWidth);
                    int lineEndInParagraph = measurer.getPosition();

                    if (posInParagraph >= lineStartInParagraph && posInParagraph <= lineEndInParagraph) {
                        cursorColumn = posInParagraph - lineStartInParagraph;
                        return;
                    }

                    lineStartInParagraph = lineEndInParagraph;
                    cursorLine++;
                }
                cursorColumn = posInParagraph - lineStartInParagraph;
                return;
            }

            charCount += paragraph.length() + 1;

            if (boxWidth > 0 && !paragraph.isEmpty()) {
                AttributedString attributedString = new AttributedString(paragraph);
                attributedString.addAttribute(TextAttribute.FONT, font);

                AttributedCharacterIterator iterator = attributedString.getIterator();
                LineBreakMeasurer measurer = new LineBreakMeasurer(iterator, frc);

                while (measurer.getPosition() < paragraph.length()) {
                    measurer.nextLayout((float) boxWidth);
                    cursorLine++;
                }
            }
            cursorLine++;
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        updateWrappedLines();
        updateCursorPosition();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Shape transformedShape = this.getTransformedShape();

        boolean shouldDrawBox = isEditing || (text.isEmpty() && isSelected()) ||
                (rectangle.getWidth() > 0 && rectangle.getHeight() > 0 && text.isEmpty());

        if (shouldDrawBox) {
            graphics2D.draw(transformedShape);
        }

        if (!text.isEmpty() || isEditing) {
            drawText(graphics2D);
        }

        if (isEditing) {
            updateCursorBlink();
            if (cursorVisible) {
                drawCursor(graphics2D);
            }
        }

        if (isSelected()) {
            this.setAnchors();

            for (int i = 0; i < this.getAnchors().length; i++) {
                Color penColor = graphics2D.getColor();
                graphics2D.setColor(graphics2D.getBackground());
                graphics2D.fill(this.getAnchors()[i]);
                graphics2D.setColor(penColor);
                graphics2D.draw(this.getAnchors()[i]);
            }
        }
    }

    private void drawText(Graphics2D graphics2D) {
        AffineTransform originalGTransform = graphics2D.getTransform();

        AffineTransform shapeTransform = this.getAffineTransform();
        double[] matrix = new double[6];
        shapeTransform.getMatrix(matrix);

        double rotationAngle = Math.atan2(matrix[1], matrix[0]);

        double effectiveScaleX = (matrix[0] * Math.cos(rotationAngle) + matrix[1] * Math.sin(rotationAngle));
        double effectiveScaleY = (matrix[2] * -Math.sin(rotationAngle) + matrix[3] * Math.cos(rotationAngle));

        boolean isFlippedX = effectiveScaleX < 0;
        boolean isFlippedY = effectiveScaleY < 0;

        double scaleX_mag = Math.sqrt(matrix[0] * matrix[0] + matrix[1] * matrix[1]);
        double scaleY_mag = Math.sqrt(matrix[2] * matrix[2] + matrix[3] * matrix[3]);

        double logicalScaledWidth = rectangle.getWidth() * scaleX_mag;
        double logicalScaledHeight = rectangle.getHeight() * scaleY_mag;

        // 원본 직사각형의 좌상단 좌표 고정 사용
        Point2D sourceRectTopLeft = new Point2D.Double(rectangle.getMinX(), rectangle.getMinY());
        Point2D transformedRectTopLeft = new Point2D.Double();
        shapeTransform.transform(sourceRectTopLeft, transformedRectTopLeft);

        AffineTransform contentDrawingTransform = new AffineTransform();
        contentDrawingTransform.translate(transformedRectTopLeft.getX(), transformedRectTopLeft.getY());
        contentDrawingTransform.rotate(rotationAngle);

        // 좌우 반전 시에만 X축 반전 (텍스트는 상하 반전 안함)
        if (isFlippedX) {
            contentDrawingTransform.scale(-1, 1);
            contentDrawingTransform.translate(-logicalScaledWidth, 0);
        }

        // 상하 반전 시 Y축 반전하되, 텍스트를 윗변 기준으로 대칭 이동
        if (isFlippedY) {
            contentDrawingTransform.scale(1, -1);
            contentDrawingTransform.translate(0, -logicalScaledHeight);
        }

        graphics2D.setTransform(contentDrawingTransform);
        graphics2D.setFont(font);

        FontMetrics fm = graphics2D.getFontMetrics();
        int lineHeight = fm.getHeight();

        double startY = PADDING;

        // 상하 반전 시 텍스트를 박스의 윗변에서 시작하도록 조정
        if (isFlippedY) {
            startY = logicalScaledHeight - PADDING - (wrappedLines.size() * lineHeight);
        }

        double currentY = startY;

        for (int i = 0; i < wrappedLines.size(); i++) {
            String line = wrappedLines.get(i);
            double lineX = PADDING;

            double lineY = currentY + fm.getAscent();
            graphics2D.drawString(line, (float) lineX, (float) lineY);
            currentY += lineHeight;
        }

        graphics2D.setTransform(originalGTransform);
    }

    private void drawCursor(Graphics2D graphics2D) {
        AffineTransform originalGTransform = graphics2D.getTransform();

        AffineTransform shapeTransform = this.getAffineTransform();
        double[] matrix = new double[6];
        shapeTransform.getMatrix(matrix);

        double rotationAngle = Math.atan2(matrix[1], matrix[0]);

        double effectiveScaleX = (matrix[0] * Math.cos(rotationAngle) + matrix[1] * Math.sin(rotationAngle));
        double effectiveScaleY = (matrix[2] * -Math.sin(rotationAngle) + matrix[3] * Math.cos(rotationAngle));

        boolean isFlippedX = effectiveScaleX < 0;
        boolean isFlippedY = effectiveScaleY < 0;

        double scaleX_mag = Math.sqrt(matrix[0] * matrix[0] + matrix[1] * matrix[1]);
        double scaleY_mag = Math.sqrt(matrix[2] * matrix[2] + matrix[3] * matrix[3]);

        double logicalScaledWidth = rectangle.getWidth() * scaleX_mag;
        double logicalScaledHeight = rectangle.getHeight() * scaleY_mag;

        Point2D sourceRectTopLeft = new Point2D.Double(rectangle.getMinX(), rectangle.getMinY());
        Point2D transformedRectTopLeft = new Point2D.Double();
        shapeTransform.transform(sourceRectTopLeft, transformedRectTopLeft);

        AffineTransform cursorDrawingTransform = new AffineTransform();
        cursorDrawingTransform.translate(transformedRectTopLeft.getX(), transformedRectTopLeft.getY());
        cursorDrawingTransform.rotate(rotationAngle);

        if (isFlippedX) {
            cursorDrawingTransform.scale(-1, 1);
            cursorDrawingTransform.translate(-logicalScaledWidth, 0);
        }

        if (isFlippedY) {
            cursorDrawingTransform.scale(1, -1);
            cursorDrawingTransform.translate(0, -logicalScaledHeight);
        }

        graphics2D.setTransform(cursorDrawingTransform);
        graphics2D.setFont(font);
        FontMetrics fm = graphics2D.getFontMetrics();
        int lineHeight = fm.getHeight();

        double cursorX = PADDING;
        double startY = PADDING;

        // 상하 반전 시 커서도 동일하게 조정
        if (isFlippedY) {
            startY = logicalScaledHeight - PADDING - (wrappedLines.size() * lineHeight);
        }

        double cursorYTop = startY;

        if (cursorLine < wrappedLines.size()) {
            String line = wrappedLines.get(cursorLine);
            String beforeCursor = line.substring(0, Math.min(cursorColumn, line.length()));

            cursorX += fm.stringWidth(beforeCursor);

            cursorYTop += cursorLine * lineHeight;
            double cursorYBottom = cursorYTop + lineHeight;

            graphics2D.drawLine((int) cursorX, (int) cursorYTop,
                    (int) cursorX, (int) cursorYBottom);
        }

        graphics2D.setTransform(originalGTransform);
    }

    private void updateCursorBlink() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBlinkTime >= CURSOR_BLINK_INTERVAL) {
            cursorVisible = !cursorVisible;
            lastBlinkTime = currentTime;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.cursorPosition = text.length();
        updateWrappedLines();
        updateCursorPosition();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        updateWrappedLines();
        updateCursorPosition();
    }
}