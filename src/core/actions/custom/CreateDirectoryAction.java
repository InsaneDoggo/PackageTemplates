package core.actions.custom;

import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateDirectoryAction extends SimpleAction {

    private File fileDir;

    public CreateDirectoryAction(File fileDir) {
        this.fileDir = fileDir;
    }

    @Override
    public boolean run() {
        isDone = FileWriter.createDirectory(fileDir);
        return isDone;
    }

    @Override
    public boolean undo() {
        isDone = !FileWriter.removeDirectory(fileDir);
        return !isDone;
    }
}
