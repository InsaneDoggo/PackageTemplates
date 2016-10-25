package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.SelectPackageTemplateDialog;
import global.models.PackageTemplate;
import core.state.util.SaveUtil;
import global.utils.ProgressHelper;
import global.utils.i18n.Localizer;
import global.utils.WrappersFactory;
import global.wrappers.PackageTemplateWrapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    private VirtualFile virtualFile;
    private Project project;

    @Override
    public void actionPerformed(AnActionEvent event) {
//        if(true){
//            try {
//                Tester.doTest(event);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return;
//        }

        virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        project = event.getProject();

        SaveUtil.getInstance().load();
        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate pt) {
                if (pt.isSkipDefiningNames()) {
                    executeTemplateSilently(pt, project, virtualFile);
                } else {
                    showDialog(pt, project, virtualFile);
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
//                Logger.log("onSuccess");
//            }
//
//            @Override
//            public void onCancel() {
//                Logger.log("onCancel");
//            }
//        };
//        dialog.show();
    }

    public static void executeTemplateSilently(PackageTemplate pt, Project project, VirtualFile virtualFile) {
        ProgressHelper.runProcessWithProgress(project, () -> {
            PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.USAGE);
            ptWrapper.prepareGlobals();
            ptWrapper.addGlobalVariablesToFileTemplates();
            ptWrapper.replaceNameVariable();
            ptWrapper.runElementsGroovyScript();
            ptWrapper.writeTemplate(project, virtualFile);
        });
    }

    public static void showDialog(PackageTemplate packageTemplate, Project project, VirtualFile virtualFile) {
        new ImplementDialog(project, String.format(Localizer.get("NewPackageFromS"),
                packageTemplate.getName()), packageTemplate, virtualFile) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
                ProgressHelper.runProcessWithProgress(project, () -> ptWrapper.writeTemplate(project, virtualFile));
            }
        }.show();
    }

}
