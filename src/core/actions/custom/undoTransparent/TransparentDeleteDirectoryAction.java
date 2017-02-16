package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class TransparentDeleteDirectoryAction extends SimpleAction {

    private File fileDirToDelete;

    public TransparentDeleteDirectoryAction(File fileDirToDelete) {
        this.fileDirToDelete = fileDirToDelete;
    }

    @Override
    public boolean run() {
        if (!FileWriter.removeDirectory(fileDirToDelete)) {
            isDone = false;
            return false;
        }

        isDone = true;
        return super.run();
    }

}
