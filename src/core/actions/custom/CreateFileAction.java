package core.actions.custom;

import core.actions.custom.base.SimpleAction;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileAction extends SimpleAction {

    private File file;
    private String content;

    public CreateFileAction(File file, String content) {
        this.file = file;
        this.content = content;
    }

    @Override
    public boolean run(SimpleAction parentAction) {
       if(!FileWriter.writeStringToFile(content, file)){
           isDone = false;
           return false;
       }

        return super.run(this);
    }

    @Override
    public boolean undo(SimpleAction parentAction) {
        if(!super.undo(this)){ return false; }

        isDone = FileWriter.removeFile(file);
        return !isDone;
    }
}
