package core.actions.newPackageTemplate.dialogs.configure;

import global.models.PackageTemplate;
import global.wrappers.PackageTemplateWrapper;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface ConfigureView {

    void setTitle(String title);
    void buildView(PackageTemplateWrapper ptWrapper);
    void onSuccess(PackageTemplate packageTemplate);
    void onFail();

}
