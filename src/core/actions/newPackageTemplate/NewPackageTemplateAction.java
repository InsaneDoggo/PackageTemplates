package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import core.actions.newPackageTemplate.dialogs.SelectPackageTemplateDialog;
import global.models.PackageTemplate;
import core.state.SaveUtil;
import global.utils.Localizer;
import global.utils.WrappersFactory;
import global.wrappers.PackageTemplateWrapper;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    private VirtualFile virtualFile;
    private Project project;

    @Override
    public void actionPerformed(AnActionEvent event) {
        virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        project = event.getProject();

        SaveUtil.getInstance().load();
        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate pt) {
                if (pt.isSkipDefiningNames()) {
                    executeTemplateSilently(pt);
                } else {
                    showDialog(pt);
                }
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

    private void executeTemplateSilently(PackageTemplate pt) {
        PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.USAGE);
        ptWrapper.prepareGlobals();
        ptWrapper.addGlobalVariablesToFileTemplates();
        ptWrapper.replaceNameVariable();
        ptWrapper.runElementsGroovyScript();
        ptWrapper.writeTemplate(project, virtualFile);
    }

    private void showDialog(PackageTemplate packageTemplate) {
        new ImplementDialog(project, String.format(Localizer.get("NewPackageFromS"),
                packageTemplate.getName()), packageTemplate, virtualFile) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
                ptWrapper.writeTemplate(project, virtualFile);
            }

            @Override
            public void onCancel() {
            }
        }.show();
    }

}
