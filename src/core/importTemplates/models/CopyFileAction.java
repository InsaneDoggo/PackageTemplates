package core.importTemplates.models;

import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CopyFileAction {

    boolean isEnaled;
    File fileFrom;
    File fileTo;

    public boolean run() {
        FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath());
        return true;
    }

}
