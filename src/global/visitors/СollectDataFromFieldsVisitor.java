package global.visitors;

import base.ElementVisitor;
import global.models.Directory;
import global.models.File;
import global.wrappers.*;

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

        directory.setName(wrapper.etfDescription.getText());

        for (ElementWrapper elementWrapper : wrapper.getListElementWrapper()) {
            elementWrapper.accept(this);
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        file.setTemplateName(wrapper.jlName.getText());
        file.setName(wrapper.etfDescription.getText());

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

    @Override
    public void visit(BinaryFileWrapper wrapper) {
        //todo impl visit collect data
    }

}
