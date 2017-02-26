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
public class TransparentCreateFileAction extends SimpleAction {

    private File file;
    private String content;

    public TransparentCreateFileAction(File file, String content) {
        this.file = file;
        this.content = content;
    }

    @Override
    public void doRun() {
        if(file.exists()){
            //todo ask
            Logger.log("TransparentCreateFileAction file Exists");
        }

        if(!FileWriter.writeStringToFile(content, file)){
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
