package core.state.export.dialogs;

import com.intellij.openapi.ui.ValidationInfo;
import core.state.SaveUtil;
import core.state.export.models.ExpPackageTemplateWrapper;
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
            ExpPackageTemplateWrapper eptWrapper = new ExpPackageTemplateWrapper(true, pt);
            listExpPackageTemplateWrapper.add(eptWrapper);
        }

        view.addExportTab(listExpPackageTemplateWrapper);
        view.addImportTab();
    }

    public void onExit(int exitCode, String savePath) {
        switch (exitCode) {
            case ImpexDialog.OK_EXIT_CODE:
                String content = SaveUtil.getInstance().getTemplatesForExport();
                FileWriter.exportFile(savePath, "Templates.json", content);
                view.onSuccess();
                break;
            case ImpexDialog.CANCEL_EXIT_CODE:
                view.onCancel();
                break;
        }
    }
}
