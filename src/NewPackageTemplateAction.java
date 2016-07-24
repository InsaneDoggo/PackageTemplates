import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiDirectory;
import custom.dialogs.NewPackageDialog;
import custom.dialogs.SelectPackageTemplateDialog;
import groovy.GroovyDialog;
import models.PackageTemplate;
import utils.Localizer;
import wrappers.PackageTemplateWrapper;
import state.SaveUtil;
import utils.FileWriter;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {

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
        new NewPackageDialog(event.getProject(), String.format(Localizer.get("NewPackageFromS"),packageTemplate.getName()), packageTemplate) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
                PsiDirectory currentDir = FileWriter.findCurrentDirectory(event);
                if (currentDir != null) {
                    ptWrapper.getRootElement().writeFile(currentDir, event.getProject());
                }
            }

            @Override
            public void onCancel() {
            }
        }.show();
    }

}
