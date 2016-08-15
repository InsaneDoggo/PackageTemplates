import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import custom.dialogs.NewPackageDialog;
import custom.dialogs.SelectPackageTemplateDialog;
import models.PackageTemplate;
import state.SaveUtil;
import utils.Localizer;
import wrappers.PackageTemplateWrapper;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {

        SaveUtil.getInstance().load();
        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                showDialog(event, packageTemplate);
            }

            @Override
            public void onCancel() {
            }
        };
        dialog.show();

    }

    private void showDialog(AnActionEvent event, PackageTemplate packageTemplate) {
        new NewPackageDialog(event, String.format(Localizer.get("NewPackageFromS"),packageTemplate.getName()), packageTemplate) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
                ptWrapper.writeTemplate(event);
            }

            @Override
            public void onCancel() {
            }
        }.show();
    }

}
