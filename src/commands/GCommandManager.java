package commands;

import states.GState;
import types.GStateType;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GCommandManager {
    private Stack<GCommand> undoStack = new Stack<>();
    private Stack<GCommand> redoStack = new Stack<>();

    public void execute(GCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();  // 새 커맨드 실행하면 redo 스택은 클리어
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            GCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("멈춰");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            GCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        } else {
            System.out.println("멈춰");
        }
    }
}
