package core.actions.newPackageTemplate.dialogs.implement;

import global.wrappers.PackageTemplateWrapper;

/**
 * Created by Arsen on 11.09.2016.
 */
public interface ImplementView {
    void buildView(PackageTemplateWrapper ptWrapper);
    void onSuccess(PackageTemplateWrapper ptWrapper);
    void onCancel();
}
