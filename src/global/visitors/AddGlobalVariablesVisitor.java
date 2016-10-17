package global.visitors;

import base.ElementVisitor;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;
import global.wrappers.PackageTemplateWrapper;

/**
 * Добавляет глобальные переменные в каждый FileTemplate.
 */
public class AddGlobalVariablesVisitor implements ElementVisitor {

    @Override
    public void visit(DirectoryWrapper wrapper) {
        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        if (wrapper.getPackageTemplateWrapper().getMode() == PackageTemplateWrapper.ViewMode.USAGE) {
            wrapper.getFile().getMapProperties().putAll(wrapper.getPackageTemplateWrapper().getPackageTemplate().getMapGlobalVars());
        }
    }

}
