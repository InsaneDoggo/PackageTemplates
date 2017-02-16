package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import global.utils.Logger;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class TransparentCreateSimpleDirectoryAction extends SimpleAction {

    private File directory;

    public TransparentCreateSimpleDirectoryAction(File directory) {
        this.directory = directory;
    }

    @Override
    public boolean run() {
        if (directory.exists()) {
            //todo ask
            Logger.log("TransparentCreateSimpleDirectoryAction dir Exists");
        }

        if (!FileWriter.createDirectories(directory.toPath())) {
            isDone = false;
            return false;
        }

        isDone = true;
        return super.run();
    }

}
