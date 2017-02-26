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
public class TransparentCreateSimpleDirectoryAction extends SimpleAction {

    private File directory;

    public TransparentCreateSimpleDirectoryAction(File directory) {
        this.directory = directory;
    }

    @Override
    public void doRun() {
        if (directory.exists()) {
            //todo ask
            Logger.log("TransparentCreateSimpleDirectoryAction dir Exists");
        }

        if (!FileWriter.createDirectories(directory.toPath())) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
