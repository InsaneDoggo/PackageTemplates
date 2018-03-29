package core.actions.custom.undoable;

import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import core.actions.custom.base.SimpleUndoableAction;
import core.actions.custom.interfaces.IHasWriteRules;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.writeRules.WriteRules;
import global.models.BaseElement;
import global.models.BinaryFile;
import global.utils.Logger;
import global.utils.file.FileWriter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

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
        if (!FileWriter.removeFile(fileTo)) {
            //todo print fail?
            Logger.log("undo ");
        }
    }

    @Override
    public void redo() throws UnexpectedUndoException {
        if(!FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath())){
            //todo print fail?
            Logger.log("redo ");
        }
    }


}
