package core.actions.custom;

import global.utils.file.FileReaderUtil;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class DeleteFileAction extends SimpleAction {

    private File fileToDelete;

    private String pathToRestore;
    private String contentToRestore;

    public DeleteFileAction(File fileToDelete) {
        this.fileToDelete = fileToDelete;
    }

    @Override
    public boolean run() {
        // save temp data
        pathToRestore = fileToDelete.getPath();

        if (fileToDelete.isDirectory()) {
            removeDir();
        } else {
            removeFile();
        }
        return isDone;
    }

    private void removeDir() {
        //todo save temp dir include files/subdirs
        isDone = FileWriter.removeDirectory(fileToDelete);
    }

    private void removeFile() {
        contentToRestore = FileReaderUtil.readFile(fileToDelete);
        if (contentToRestore == null) {
            throw new RuntimeException("Saving contentToRestore Failed");
        }
        isDone = FileWriter.removeFile(fileToDelete);
    }

    @Override
    public boolean undo() {
        if (fileToDelete.isDirectory()) {
            //todo restore temp dir include files/subdirs
            isDone = !FileWriter.createDirectory(fileToDelete);
        } else {
            isDone = !FileWriter.writeStringToFile(contentToRestore, pathToRestore);
        }
        return !isDone;
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public File getFileToDelete() {
        return fileToDelete;
    }

    public String getPathToRestore() {
        return pathToRestore;
    }

    public String getContentToRestore() {
        return contentToRestore;
    }
}
