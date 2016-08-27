package core.actions.newPackageTemplate.dialogs;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import global.models.PackageTemplate;
import global.utils.Localizer;
import global.wrappers.PackageTemplateWrapper;
import global.utils.GridBagFactory;
import global.utils.WrappersFactory;

import java.awt.*;

/**
 * Created by CeH9 on 22.06.2016.
 */

public abstract class ConfigurePackageTemplatesDialog extends BaseDialog {

    private PackageTemplateWrapper ptWrapper;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onFail();

    public ConfigurePackageTemplatesDialog(Project project) {
        super(project);
        ptWrapper = WrappersFactory.createAndWrapPackageTemplate(project, PackageTemplateWrapper.ViewMode.CREATE);
    }

    public ConfigurePackageTemplatesDialog(Project project, PackageTemplate packageTemplate) {
        super(project);
        ptWrapper = WrappersFactory.wrapPackageTemplate(project, packageTemplate, PackageTemplateWrapper.ViewMode.EDIT);
    }

    @Override
    public void preShow() {
        switch (ptWrapper.getMode()){
            case CREATE: setTitle(Localizer.get("NewPackageTemplate")); break;
            case EDIT: setTitle(Localizer.get("EditPackageTemplate")); break;
        }

        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();

        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Override
    public void onOKAction() {
        ptWrapper.collectDataFromFields();
        onSuccess(ptWrapper.getPackageTemplate());
    }

    @Override
    public void onCancelAction() {
        onFail();
    }
}
