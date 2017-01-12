package core.importTemplates.models;

import core.actions.custom.SimpleAction;
import global.utils.file.FileWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CopyFileAction extends SimpleAction {

    private File fileFrom;
    private File fileTo;

    public CopyFileAction(File fileFrom, File fileTo) {
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
    }

    @Override
    public boolean run() {
        isDone = FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath());
        return isDone;
    }

    @Override
    public boolean undo() {
        try {
            Files.delete(fileTo.toPath());
            isDone = false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
