package core.actions.custom;

import global.utils.Logger;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        try {
            // save temp data
            pathToRestore = fileToDelete.getPath();
            contentToRestore = FileReaderUtil.readFile(fileToDelete);
            if(contentToRestore == null){
                throw new RuntimeException("Saving contentToRestore Failed");
            }

            //delete
            Files.delete(fileToDelete.toPath());
            isDone = true;
        } catch (IOException e) {
            Logger.log("DeleteFileAction run ex: " + e.getMessage());
            isDone = false;
        }
        return isDone;
    }

    @Override
    public boolean undo() {
        boolean isRestored = FileWriter.writeStringToFile(contentToRestore, pathToRestore);
        isDone = !isRestored;
        return isRestored;
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
