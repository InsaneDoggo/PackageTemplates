package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import global.utils.Logger;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class TransparentCopyFileAction extends SimpleAction {

    private File fileFrom;
    private File fileTo;

    public TransparentCopyFileAction(File fileFrom, File fileTo) {
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
    }

    @Override
    public boolean run() {
        if(fileTo.exists()){
            //todo ask
            Logger.log("TransparentCopyFileAction file Exists");
        }

        if(!FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath())){
            isDone = false;
            return false;
        }

        isDone = true;
        return super.run();
    }

}
