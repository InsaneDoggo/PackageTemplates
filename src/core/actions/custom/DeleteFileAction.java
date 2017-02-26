package core.actions.custom;

import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class DeleteFileAction extends SimpleAction {

    private File fileToDelete;

    public DeleteFileAction(File fileToDelete) {
        this.fileToDelete = fileToDelete;
    }

    @Override
    public void doRun() {
        if (!FileWriter.removeFile(fileToDelete)) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public File getFileToDelete() {
        return fileToDelete;
    }

}
