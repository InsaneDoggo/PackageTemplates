package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import global.models.Favourite;
import global.models.PackageTemplate;

/**
 * Created by Arsen on 17.09.2016.
 */
public interface SelectPackageTemplateView {

    void setTitle(String title);

    void onSuccess(PackageTemplate packageTemplate);

    void onCancel();

    void updateFavouritesUI();

    void selectFavourite(Favourite selected);

    void setPathBtnText(String path);

    void hideDialog();
}
