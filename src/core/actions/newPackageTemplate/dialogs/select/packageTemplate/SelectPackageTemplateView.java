package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import global.models.PackageTemplate;
import global.models.TemplateListModel;

/**
 * Created by Arsen on 17.09.2016.
 */
public interface SelectPackageTemplateView {

    void setTitle(String title);

    void onSuccess(PackageTemplate packageTemplate);

    void onCancel();

    void setTemplatesList(TemplateListModel<PackageTemplate> list);
}
