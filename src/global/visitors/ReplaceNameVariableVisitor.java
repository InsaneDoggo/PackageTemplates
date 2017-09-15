package global.visitors;

import base.ElementVisitor;
import core.search.SearchAction;
import core.search.customPath.CustomPath;
import core.textInjection.TextInjection;
import core.textInjection.VelocityHelper;
import global.models.BinaryFile;
import global.models.Directory;
import global.models.File;
import global.wrappers.BinaryFileWrapper;
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

        directory.setName(VelocityHelper.fromTemplate(directory.getName(), mapGlobalVars));

        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }

        this.visitCustomPath(directory.getCustomPath());
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        file.setName(VelocityHelper.fromTemplate(file.getName(), mapGlobalVars));

        this.visitCustomPath(file.getCustomPath());
    }

    @Override
    public void visit(BinaryFileWrapper wrapper) {
        BinaryFile binaryFile = wrapper.getBinaryFile();
        this.visitCustomPath(binaryFile.getCustomPath());
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void visitCustomPath(CustomPath customPath) {
        if (customPath == null) {
            return;
        }

        for (SearchAction searchAction : customPath.getListSearchAction()) {
            searchAction.setName(VelocityHelper.fromTemplate(searchAction.getName(), mapGlobalVars));
        }
    }

    public void visitTextToSearch(TextInjection textInjection) {
        if(textInjection.isRegexp()){
            return;
        }

        textInjection.setTextToSearch(VelocityHelper.fromTemplate(textInjection.getTextToSearch(), mapGlobalVars));
    }
}
