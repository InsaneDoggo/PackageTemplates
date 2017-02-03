package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.SelectPackageTemplateDialog;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.factories.WrappersFactory;
import global.utils.i18n.Localizer;
import global.wrappers.PackageTemplateWrapper;

import java.util.ArrayList;
import java.util.List;

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

        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate pt) {
                if (pt.isSkipDefiningNames()) {
                    executeTemplateSilently(pt, project, virtualFile);
                } else {
                    showDialog(pt, project, virtualFile);
                }
            }
        };
        dialog.show();
    }

    public static void executeTemplateSilently(PackageTemplate pt, Project project, VirtualFile virtualFile) {
//        ProgressHelper.runProcessWithProgress(project, () -> {
        PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.USAGE);
        ptWrapper.prepareGlobals();
        ptWrapper.addGlobalVariablesToFileTemplates();
        ptWrapper.replaceNameVariable();
        ptWrapper.runElementsGroovyScript();

        collectAndExecuteActions(project, virtualFile, ptWrapper);
//        });
    }

    public static void showDialog(PackageTemplate packageTemplate, Project project, VirtualFile virtualFile) {
        new ImplementDialog(project, String.format(Localizer.get("NewPackageFromS"),
                packageTemplate.getName()), packageTemplate, virtualFile) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
//                ProgressHelper.runProcessWithProgress(project, () -> {
                collectAndExecuteActions(project, virtualFile, ptWrapper);
//                });
            }
        }.show();
    }

    private static void collectAndExecuteActions(Project project, VirtualFile virtualFile, PackageTemplateWrapper ptWrapper) {
        List<SimpleAction> listSimpleAction = new ArrayList<>();
        ptWrapper.collectSimpleActions(project, virtualFile, listSimpleAction);

        if (ActionExecutor.runAsTransaction(project, listSimpleAction, "ExecutePackageTemplate", AccessPrivileges.WRITE)) {
            Logger.log("ExecutePackageTemplate  Done!");
        } else {
            Logger.log("ExecutePackageTemplate  Fail!");
            //todo error dialog with info
            Messages.showErrorDialog("Execution Failed.\nYou can use Ctrl+Z or 'Undo Action' from toolbar to revert changes.", "PackageTemplate Failed");

//            SimpleAction simpleAction = new SimpleAction() {
//                @Override
//                public boolean run(SimpleAction parentAction) {
//                    UndoManager.getInstance(project).undo(null);
//                    return true;
//                }
//            };
//
//            ActionExecutor.runAction(project, simpleAction, "UndoActionTest", AccessPrivileges.WRITE);
        }
    }

}
