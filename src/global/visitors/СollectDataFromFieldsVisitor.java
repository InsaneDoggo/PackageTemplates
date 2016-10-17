package global.visitors;

import base.ElementVisitor;
import global.models.Directory;
import global.models.File;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;
import global.wrappers.PackageTemplateWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Сохраняет значения из UI элементов в соответствующие контейнеры.
 */
public class СollectDataFromFieldsVisitor implements ElementVisitor {

    @Override
    public void visit(DirectoryWrapper wrapper) {
        Directory directory = wrapper.getDirectory();

        directory.setName(wrapper.etfName.getText());

        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        file.setTemplateName(wrapper.jlName.getText());
        file.setName(wrapper.etfName.getText());

        if (wrapper.getPackageTemplateWrapper().getMode() == PackageTemplateWrapper.ViewMode.USAGE) {
            file.setMapProperties(new HashMap<>());
            if (wrapper.panelVariables != null) {
                Properties properties = new Properties();
                properties = wrapper.panelVariables.getProperties(properties);

                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    file.getMapProperties().put((String) entry.getKey(), (String) entry.getValue());
                }
            }
        }
    }

}
