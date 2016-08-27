package core.state.impex.dialogs;

import core.state.impex.models.ExpPackageTemplateWrapper;

import java.util.ArrayList;

/**
 * Created by Arsen on 27.08.2016.
 */
public interface ImpexView {

    void addImportTab();

    void addExportTab(ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper);

    void onSuccess();

    void onCancel();

}
