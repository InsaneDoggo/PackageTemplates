package core.actions.custom.undoTransparent;

import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
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
    public void doRun() {
        if (!FileWriter.removeDirectory(fileDirToDelete)) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
