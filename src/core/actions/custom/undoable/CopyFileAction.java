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
public class CopyFileAction extends SimpleUndoableAction implements IHasWriteRules {

    private BinaryFile file;
    private File fileFrom;

    public CopyFileAction(Project project, BinaryFile file) {
        super(project);
        this.file = file;
        fileFrom = new File(file.getSourcePath());
    }

    @Override
    public void undo() throws UnexpectedUndoException {
//        if (!FileWriter.removeFile(fileTo)) {
//            //nothing
//        }
    }

    @Override
    public void redo() throws UnexpectedUndoException {

    }

    private boolean createBinaryFile(File fileTo) {
        return FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath());
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public WriteRules getWriteRules() {
        return file.getWriteRules();
    }

    @Nullable
    private String getCustomPath(BaseElement element, String pathFrom) {
        ArrayList<SearchAction> actions = element.getCustomPath().getListSearchAction();

        java.io.File searchResultFile = SearchEngine.find(new java.io.File(pathFrom), actions);

        if (searchResultFile == null) {
            //print name of last action
            Logger.log("getCustomPath File Not Found: " + (actions.isEmpty() ? "" : actions.get(actions.size() - 1).getName()));
            return null;
        }

        return searchResultFile.getPath();
    }

    private java.io.File getDuplicateFile(String path) {
        return new java.io.File(path + java.io.File.separator
                + fileFrom.getName());
    }

    @Override
    public String toString() {
        return String.format("%s:  %s",
                getClass().getSimpleName(),
                fileFrom.getName()
        );
    }

}
