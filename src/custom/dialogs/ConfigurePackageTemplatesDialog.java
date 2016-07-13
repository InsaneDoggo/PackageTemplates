package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import models.TemplateContainer;
import reborn.models.Directory;
import reborn.models.GlobalVariable;
import reborn.wrappers.DirectoryWrapper;
import reborn.wrappers.GlobalVariableWrapper;
import reborn.wrappers.PackageTemplateWrapper;
import utils.GridBagFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CeH9 on 22.06.2016.
 */

public abstract class ConfigurePackageTemplatesDialog extends BaseDialog {

    private PackageTemplateWrapper ptWrapper;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onFail();

    public ConfigurePackageTemplatesDialog(Project project) {
        super(project);
        ptWrapper = new PackageTemplateWrapper(project);

        Directory directory = new Directory();
        directory.setName("example");
        directory.setListBaseElement(new ArrayList<>());
        directory.setEnabled(true);
        directory.setGroovyCode("");

        PackageTemplate packageTemplate = new PackageTemplate();
        packageTemplate.setMapGlobalVars(new HashMap<>());
        packageTemplate.setListGlobalVariable(new ArrayList<>());
        packageTemplate.setName("New Package Template");
        packageTemplate.setDescription("");
        packageTemplate.setDirectory(directory);

        DirectoryWrapper dirWrapper = new DirectoryWrapper();
        dirWrapper.setListElementWrapper(new ArrayList<>());
        dirWrapper.setPackageTemplateWrapper(ptWrapper);
        dirWrapper.setParent(null);
        dirWrapper.setDirectory(directory);

        GlobalVariable globalVariable = new GlobalVariable();
        globalVariable.setName(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME);
        globalVariable.setValue("Example");
        globalVariable.setEnabled(true);
        globalVariable.setGroovyCode("");

        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.setRootElement(dirWrapper);
        ptWrapper.setProject(project);
        ptWrapper.setMode(PackageTemplateWrapper.ViewMode.CREATE);
        ptWrapper.setListGlobalVariableWrapper(new ArrayList<>());
        ptWrapper.addGlobalVariable(new GlobalVariableWrapper(globalVariable));
    }

    public ConfigurePackageTemplatesDialog(Project project, PackageTemplate packageTemplate) {
        super(project);
        ptWrapper = new PackageTemplateWrapper(project);
        ptWrapper.setMode(PackageTemplateWrapper.ViewMode.EDIT);
        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.wrapGlobals();
        ptWrapper.setRootElement(ptWrapper.wrapDirectory(packageTemplate.getDirectory(), null));
        ptWrapper.setProject(project);
    }

    @Override
    void preShow() {
        switch (ptWrapper.getMode()){
            case CREATE: setTitle("New Package Template"); break;
            case EDIT: setTitle("Edit Package Template"); break;
        }

        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();

        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Override
    void onOKAction() {
        ptWrapper.collectDataFromFields();
        onSuccess(ptWrapper.getPackageTemplate());
    }

    @Override
    void onCancelAction() {
        onFail();
    }
}
