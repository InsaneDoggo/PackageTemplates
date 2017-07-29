package core.actions.newPackageTemplate.dialogs.select.binaryFile;

import global.utils.i18n.Localizer;

/**
 * Created by Arsen on 16.09.2016.
 */
public class SelectBinaryFilePresenterImpl implements SelectBinaryFilePresenter {

    private SelectBinaryFileDialog view;

    public SelectBinaryFilePresenterImpl(SelectBinaryFileDialog view) {
        this.view = view;
        view.setTitle(Localizer.get("title.SelectBinaryFile"));
    }

}
