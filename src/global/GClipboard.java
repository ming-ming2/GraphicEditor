package global;

import shapes.GShape;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GClipboard {
    private static GClipboard instance;
    private List<GShape> clipboardShapes;
    private int pasteCount;
    private static final int PASTE_OFFSET = GConstants.EClipboardConstants.ePasteOffset.getValue();

    private GClipboard() {
        clipboardShapes = new ArrayList<>();
        pasteCount = 0;
    }

    public static GClipboard getInstance() {
        if (instance == null) {
            instance = new GClipboard();
        }
        return instance;
    }

    public void copy(List<GShape> shapes) {
        clipboardShapes.clear();
        pasteCount = 0;
        try {
            for (GShape shape : shapes) {
                GShape clonedShape = deepClone(shape);
                clipboardShapes.add(clonedShape);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GShape> paste() {
        List<GShape> pastedShapes = new ArrayList<>();
        try {
            pasteCount++;
            int offsetX = PASTE_OFFSET * pasteCount;
            int offsetY = PASTE_OFFSET * pasteCount;

            for (GShape shape : clipboardShapes) {
                GShape clonedShape = deepClone(shape);
                clonedShape.getAffineTransform().translate(offsetX, offsetY);
                pastedShapes.add(clonedShape);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pastedShapes;
    }

    public boolean hasContent() {
        return !clipboardShapes.isEmpty();
    }

    public void clear() {
        clipboardShapes.clear();
        pasteCount = 0;
    }

    private GShape deepClone(GShape shape) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(shape);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        GShape clonedShape = (GShape) ois.readObject();
        ois.close();

        return clonedShape;
    }
}