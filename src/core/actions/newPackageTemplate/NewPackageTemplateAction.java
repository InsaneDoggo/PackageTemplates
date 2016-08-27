package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import core.actions.newPackageTemplate.dialogs.ImplementPackageTemplateDialog;
import core.actions.newPackageTemplate.dialogs.SelectPackageTemplateDialog;
import global.models.PackageTemplate;
import core.state.SaveUtil;
import global.utils.Localizer;
import global.wrappers.PackageTemplateWrapper;

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


//        ImpexDialog dialog = new ImpexDialog(event.getProject(), "Export Templates") {
//            @Override
//            public void onSuccess() {
//                System.out.println("onSuccess");
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("onCancel");
//            }
//        };
//        dialog.show();

    }

    private void showDialog(AnActionEvent event, PackageTemplate packageTemplate) {
        new ImplementPackageTemplateDialog(event, String.format(Localizer.get("NewPackageFromS"),packageTemplate.getName()), packageTemplate) {
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
