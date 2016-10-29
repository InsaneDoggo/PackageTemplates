package global.visitors;

import base.ElementVisitor;
import global.utils.WrappersFactory;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;
import global.wrappers.PackageTemplateWrapper;

import java.util.HashSet;

/**
 * Вставляет переменные в имена.
 */
public class CollectFileTemplatesVisitor implements ElementVisitor {

    private HashSet<String> hsFileTemplateNames;

    public CollectFileTemplatesVisitor() {
        hsFileTemplateNames = new HashSet<>();
    }


    @Override
    public void visit(DirectoryWrapper wrapper) {
        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        hsFileTemplateNames.add(wrapper.getFile().getTemplateName());
    }

}
