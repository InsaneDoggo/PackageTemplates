package core.actions.newPackageTemplate.dialogs.configure;

import com.intellij.openapi.project.Project;
import global.models.PackageTemplate;
import global.utils.Localizer;
import global.utils.WrappersFactory;
import global.wrappers.PackageTemplateWrapper;

/**
 * Created by Arsen on 16.09.2016.
 */
public class ConfigurePresenterImpl implements ConfigurePresenter {

    private PackageTemplateWrapper ptWrapper;
    private ConfigureView view;


    public ConfigurePresenterImpl(Project project, ConfigureView view) {
        this.ptWrapper = WrappersFactory.createAndWrapPackageTemplate(project, PackageTemplateWrapper.ViewMode.CREATE);
        this.view = view;
    }

    public ConfigurePresenterImpl(Project project, PackageTemplate packageTemplate, ConfigureView view) {
        this.view = view;
        this.ptWrapper = WrappersFactory.wrapPackageTemplate(project, packageTemplate, PackageTemplateWrapper.ViewMode.EDIT);
    }

    @Override
    public void onPreShow() {
        switch (ptWrapper.getMode()){
            case CREATE: view.setTitle(Localizer.get("NewPackageTemplate")); break;
            case EDIT: view.setTitle(Localizer.get("EditPackageTemplate")); break;
        }

        view.buildView(ptWrapper);
    }

    @Override
    public void onOKAction() {
        ptWrapper.collectDataFromFields();
        view.onSuccess(ptWrapper.getPackageTemplate());
    }

    @Override
    public void onFail() {
        view.onFail();
    }

}
