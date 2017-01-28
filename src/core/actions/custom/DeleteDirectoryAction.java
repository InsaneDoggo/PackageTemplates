package core.actions.custom;

import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class DeleteDirectoryAction extends SimpleAction {

    private File fileDirToDelete;

    private String pathToRestore;
    private String contentToRestore;

    public DeleteDirectoryAction(File fileDirToDelete) {
        this.fileDirToDelete = fileDirToDelete;
    }

    @Override
    public boolean run() {
        // save temp data
        pathToRestore = fileDirToDelete.getPath();

        //todo save temp dir include files/subdirs
        isDone = FileWriter.removeDirectory(fileDirToDelete);

        return isDone;
    }

    @Override
    public boolean undo() {
            //todo restore temp dir include files/subdirs
            isDone = !FileWriter.createDirectory(fileDirToDelete);
        return !isDone;
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public File getFileDirToDelete() {
        return fileDirToDelete;
    }

    public String getPathToRestore() {
        return pathToRestore;
    }

    public String getContentToRestore() {
        return contentToRestore;
    }
}
