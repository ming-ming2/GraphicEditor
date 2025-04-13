package commands;

public interface GCommand {
    void execute();

    void undo();

    void redo();
}
