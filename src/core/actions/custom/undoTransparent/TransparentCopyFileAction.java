package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
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
    public void doRun() {
        if(fileTo.exists()){
            //todo ask
            Logger.log("TransparentCopyFileAction file Exists");
        }

        if(!FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath())){
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
