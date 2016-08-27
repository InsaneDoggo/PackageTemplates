package core.state.impex.dialogs;

import com.intellij.openapi.ui.ValidationInfo;
import core.state.MigrationHelper;
import core.state.SaveUtil;
import core.state.impex.models.ExpPackageTemplateWrapper;
import core.state.impex.models.ExportBundle;
import core.state.models.StateModel;
import global.models.PackageTemplate;
import global.utils.FileWriter;
import global.utils.Localizer;

import java.util.ArrayList;

/**
 * Created by Arsen on 27.08.2016.
 */

public class ImpexPresenter {

    private ImpexView view;
    private StateModel stateModel;

    public ImpexPresenter(ImpexView view) {
        this.view = view;
        stateModel = SaveUtil.getInstance().getStateModel();
    }

    public ValidationInfo doValidate(String savePath){
        if (savePath.isEmpty()) {
            return new ValidationInfo(Localizer.get("FillEmptyFields"));
        }
        return null;
    }

    private ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper;

    public void onCreateCenterPanel() {
        listExpPackageTemplateWrapper = new ArrayList<>();
        for (PackageTemplate pt : stateModel.getListPackageTemplate()) {
            listExpPackageTemplateWrapper.add(new ExpPackageTemplateWrapper(true, pt));
        }

        view.addExportTab(listExpPackageTemplateWrapper);
        view.addImportTab();
    }

    public void exportTemplates(String savePath){
        FileWriter.exportFile(savePath, "Templates.json", getContent());
    }

    private String getContent() {
        ExportBundle bundle = new ExportBundle();
        bundle.setModelVersion(MigrationHelper.CURRENT_MODEL_VERSION);
        bundle.setListPackageTemplate(new ArrayList<>());

        for(ExpPackageTemplateWrapper item : listExpPackageTemplateWrapper){
            if( item.cbInclude.isSelected()){
                bundle.getListPackageTemplate().add(item.getTemplateForExport());
            }
        }
        return null;
    }

    public void onExit(int exitCode) {
        switch (exitCode) {
            case ImpexDialog.OK_EXIT_CODE:
                view.onSuccess();
                break;
            case ImpexDialog.CANCEL_EXIT_CODE:
                view.onCancel();
                break;
        }
    }
}
