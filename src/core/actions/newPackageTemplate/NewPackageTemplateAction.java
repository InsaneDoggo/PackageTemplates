package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.executor.request.ActionRequest;
import core.actions.executor.request.ActionRequestBuilder;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.SelectPackageTemplateDialog;
import core.report.ReportHelper;
import core.report.dialogs.ReportDialog;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.ProgressHelper;
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
        ptWrapper.runElementsScript();

        collectAndExecuteActions(project, virtualFile, ptWrapper);
//        });
    }

    public static void showDialog(PackageTemplate packageTemplate, Project project, VirtualFile virtualFile) {
        new ImplementDialog(project, String.format(Localizer.get("NewPackageFromS"),
                packageTemplate.getName()), packageTemplate, virtualFile) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
                collectAndExecuteActions(project, virtualFile, ptWrapper);
            }
        }.show();
    }

    private static void collectAndExecuteActions(Project project, VirtualFile virtualFile, PackageTemplateWrapper ptWrapper) {
        List<SimpleAction> listSimpleAction = new ArrayList<>();
        ptWrapper.collectSimpleActions(project, virtualFile, listSimpleAction);

        ActionRequest actionRequest = new ActionRequestBuilder()
                .setProject(project)
                .setActions(listSimpleAction)
                .setActionLabel("Execute PackageTemplate")
                .setAccessPrivileges(AccessPrivileges.WRITE)
                .setConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION)
                .setActionListener(new ActionRequest.ActionFinishListener() {
                    @Override
                    public void onFinish() {
                        if(ptWrapper.getPackageTemplate().shouldShowReport()){
                            showReportDialog(project);
                        }
                        ReportHelper.reset();

                    }

                    @Override
                    public void onFail() {
                        showReportDialog(project);
                        ReportHelper.reset();
                    }
                })
                .build();

        ActionExecutor.runAsTransaction(actionRequest);
    }

    private static void showReportDialog(final Project project) {
        new ReportDialog(project) {
            @Override
            public void onSuccess() {
            }
        }.show();
    }

}
