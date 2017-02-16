package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class TransparentDeleteFileAction extends SimpleAction {

    private File fileToDelete;

    public TransparentDeleteFileAction(File fileToDelete) {
        this.fileToDelete = fileToDelete;
    }

    @Override
    public boolean run() {
        if(!FileWriter.removeFile(fileToDelete)){
            isDone = false;
            return false;
        }

        isDone = true;
        return super.run();
    }

}
