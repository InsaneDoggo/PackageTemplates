package global.visitors;

import core.groovy.GroovyExecutor;
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

        if (directory.getGroovyCode() != null && !directory.getGroovyCode().isEmpty()) {
            directory.setName(GroovyExecutor.runGroovy(directory.getGroovyCode(), directory.getName()));
        }

        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        if (file.getGroovyCode() != null && !file.getGroovyCode().isEmpty()) {
            file.setName(GroovyExecutor.runGroovy(file.getGroovyCode(), file.getName()));
        }
    }

}
