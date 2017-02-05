package global.visitors;

import base.ElementVisitor;
import core.script.ScriptExecutor;
import global.models.Directory;
import global.models.File;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;

/**
 * Выполняет Groovy Script.
 */
public class RunGroovyScriptVisitor implements ElementVisitor {

    @Override
    public void visit(DirectoryWrapper wrapper) {
        Directory directory = wrapper.getDirectory();

        if (directory.getScript() != null && !directory.getScript().isEmpty()) {
            directory.setName(ScriptExecutor.runScript(directory.getScript(), directory.getName()));
        }

        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        if (file.getScript() != null && !file.getScript().isEmpty()) {
            file.setName(ScriptExecutor.runScript(file.getScript(), file.getName()));
        }
    }

}
