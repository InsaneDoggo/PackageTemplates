package core.actions.custom.undoable;

import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.base.SimpleUndoableAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import global.utils.Logger;
import global.utils.file.FileWriter;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CopyFileAction extends SimpleUndoableAction {

    private File fileFrom;
    private File fileTo;

    public CopyFileAction(Project project, File fileFrom, File fileTo) {
        super(project);
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
    }

    @Override
    public void undo() throws UnexpectedUndoException {
        if(!FileWriter.removeFile(fileTo)) {
            //nothing
        }
    }

    @Override
    public void redo() throws UnexpectedUndoException {
        if(fileTo.exists()){
            //todo ask
            Logger.log("TransparentCopyFileAction file Exists");
        }

        if(!FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath())){
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public String toString() {
        return String.format("%s: from %s to %s",
                getClass().getSimpleName(),
                fileFrom.getName(),
                fileTo.getName()
        );
    }

}
