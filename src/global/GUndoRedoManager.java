package global;

import shapes.GShape;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GUndoRedoManager {
    private List<Vector<GShape>> undoStack;
    private List<Vector<GShape>> redoStack;
    private static final int MAX_UNDO_SIZE = 50;

    public GUndoRedoManager() {
        undoStack = new ArrayList<>();
        redoStack = new ArrayList<>();
    }

    public void saveState(Vector<GShape> shapes) {
        try {
            Vector<GShape> clonedShapes = deepClone(shapes);
            undoStack.add(clonedShapes);

            if (undoStack.size() > MAX_UNDO_SIZE) {
                undoStack.remove(0);
            }

            redoStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<GShape> undo() {
        if (undoStack.size() <= 1) return null;

        try {
            Vector<GShape> currentState = undoStack.remove(undoStack.size() - 1);
            redoStack.add(currentState);

            Vector<GShape> previousState = undoStack.get(undoStack.size() - 1);
            return deepClone(previousState);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Vector<GShape> redo() {
        if (redoStack.isEmpty()) return null;

        try {
            Vector<GShape> redoState = redoStack.remove(redoStack.size() - 1);
            undoStack.add(redoState);

            return deepClone(redoState);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean canUndo() {
        return undoStack.size() > 1;
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    private Vector<GShape> deepClone(Vector<GShape> shapes) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(shapes);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Vector<GShape> clonedShapes = (Vector<GShape>) ois.readObject();
        ois.close();

        return clonedShapes;
    }
}