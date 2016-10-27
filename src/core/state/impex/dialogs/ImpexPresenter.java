package core.state.impex.dialogs;

import com.intellij.openapi.ui.ValidationInfo;
import core.state.util.MigrationHelper;
import core.state.util.SaveUtil;
import core.state.impex.models.ExpPackageTemplateWrapper;
import core.state.impex.models.ExportBundle;
import core.state.models.StateModel;
import global.Const;
import global.models.PackageTemplate;
import global.utils.FileWriter;
import global.utils.GsonFactory;
import global.utils.PackageTemplateHelper;
import global.utils.i18n.Localizer;

import java.util.ArrayList;

/**
 * Created by Arsen on 27.08.2016.
 */

public class ImpexPresenter {

    private ImpexView view;

    public ImpexPresenter(ImpexView view) {
        this.view = view;
    }

    public ValidationInfo doValidate(String savePath) {
        switch (curTab){
            case IMPORT:
                return validateExport(savePath);
            case EXPORT:
                return validateImport();
        }
        return null;
    }

    private ValidationInfo validateImport() {
        // TODO: 04.09.2016  validateImport
        return null;
    }

    private ValidationInfo validateExport(String savePath) {
        if (savePath.isEmpty()) {
            return new ValidationInfo(Localizer.get("warning.FillEmptyFields"));
        }
        return null;
    }

    private ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper;

    public void onCreateCenterPanel() {
        listExpPackageTemplateWrapper = new ArrayList<>();
        for (PackageTemplate pt : PackageTemplateHelper.getListPackageTemplate()) {
            listExpPackageTemplateWrapper.add(new ExpPackageTemplateWrapper(true, pt));
        }

        view.addImportTab();
        view.addExportTab(listExpPackageTemplateWrapper);
    }

    public void exportTemplates(String savePath) {
        FileWriter.exportFile(savePath, Const.EXPORT_FILE_NAME, getContent());
    }

    private String getContent() {
        ExportBundle bundle = new ExportBundle();
        bundle.setModelVersion(MigrationHelper.CURRENT_MODEL_VERSION);
        bundle.setListPackageTemplate(new ArrayList<>());

        for (ExpPackageTemplateWrapper item : listExpPackageTemplateWrapper) {
            if (item.cbInclude.isSelected()) {
                bundle.getListPackageTemplate().add(item.getTemplateForExport());
            }
        }

        return GsonFactory.getInstance().toJson(bundle, ExportBundle.class);
    }

    public enum TabType {IMPORT, EXPORT}

    TabType curTab;

    public void setCurrentTab(TabType type) {
        curTab = type;
    }

}
