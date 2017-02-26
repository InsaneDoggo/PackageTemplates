package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
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
    public void doRun() {
        if(!FileWriter.removeFile(fileToDelete)) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
