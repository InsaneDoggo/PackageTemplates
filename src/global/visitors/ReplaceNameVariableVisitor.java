package global.visitors;

import base.ElementVisitor;
import global.models.Directory;
import global.models.File;
import global.utils.text.StringTools;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;

import java.util.HashMap;

/**
 * Вставляет переменные в имена.
 */
public class ReplaceNameVariableVisitor implements ElementVisitor {

    private HashMap<String, String> mapGlobalVars;

    public ReplaceNameVariableVisitor(HashMap<String, String> mapGlobalVars) {
        this.mapGlobalVars = mapGlobalVars;
    }


    @Override
    public void visit(DirectoryWrapper wrapper) {
        Directory directory = wrapper.getDirectory();

        directory.setName(StringTools.replaceGlobalVariables(directory.getName(), mapGlobalVars));

        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        file.setName(StringTools.replaceGlobalVariables(file.getName(), mapGlobalVars));
    }

}
