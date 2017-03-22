package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import core.actions.custom.InjectTextAction;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.executor.request.ActionRequest;
import core.actions.executor.request.ActionRequestBuilder;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.SelectPackageTemplateDialog;
import core.report.ReportHelper;
import core.report.dialogs.ReportDialog;
import core.search.SearchAction;
import core.search.SearchActionType;
import core.search.SearchEngine;
import core.search.customPath.CustomPath;
import core.textInjection.InjectDirection;
import core.textInjection.TextInjection;
import core.textInjection.VelocityHelper;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.factories.WrappersFactory;
import global.utils.file.PathHelper;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    private VirtualFile virtualFile;
    private Project project;
    private String contextDirectoryPath;
    private String fullPath;

    @Override
    public void actionPerformed(AnActionEvent event) {
        virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        project = event.getProject();

//        Logger.log("actionPerformed ");
//        if (true) {
//            CommandProcessor.getInstance().executeCommand(project, () -> {
//                ApplicationManager.getApplication().runWriteAction(() -> {
//                    testCode(project, event);
//                });
//            }, "comTest", null);
//            return;
//        }

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


    //=================================================================
    //  TEST ZONE START
    //=================================================================
    private void testCode(Project project, AnActionEvent event) {
        PsiFile psiFile = PsiHelper.findPsiFileByPath(project, "E:/WORK/IdeaProjects/PluginTests/src/com/company/Main.java");
        if (psiFile == null) {
            Logger.log("Fail: findPsiFileByPath");
            return;
        }

        ReadonlyStatusHandler.OperationStatus status = ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(psiFile.getVirtualFile());
        if (status.hasReadonlyFiles()) {
            Logger.log("Fail: testCode Readonly");
            return;
        }

        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        int offset = document.getText().indexOf("line_2");
        int lineNumber = document.getLineNumber(offset);
        int lineEndOffset = document.getLineEndOffset(lineNumber);

        String textToInsert = fromVelocity("String ${DDD};");
        document.insertString(lineEndOffset, textToInsert);
    }

    @NotNull
    private String fromVelocity(String velocityTemplate) {
        HashMap<String, String> map = new HashMap<>();
        map.put("DDD", "SomeValue");

        String fromTemplate = VelocityHelper.fromTemplate(velocityTemplate, map);
        if (fromTemplate == null) {
            return "";
        }
        return fromTemplate;
    }
    //=================================================================
    //  TEST ZONE END
    //=================================================================


    private static void preExecuteTemplate(Project project, VirtualFile virtualFile, PackageTemplateWrapper ptWrapper) {
        ptWrapper.getExecutionContext().virtualFile = virtualFile;
        ptWrapper.getExecutionContext().project = project;
        ptWrapper.getExecutionContext().ctxFullPath = virtualFile.getPath();
        ptWrapper.getExecutionContext().ctxDirPath = PathHelper.toDirPath(virtualFile);
        ptWrapper.initDefaultProperties();
    }

    public static void executeTemplateSilently(PackageTemplate pt, Project project, VirtualFile virtualFile) {
        PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.USAGE);
        executeTemplate(project, virtualFile, ptWrapper);
    }

    public static void showDialog(PackageTemplate packageTemplate, Project project, VirtualFile virtualFile) {
        new ImplementDialog(project, String.format(Localizer.get("NewPackageFromS"),
                packageTemplate.getName()), packageTemplate, virtualFile) {
            @Override
            public void onSuccess(PackageTemplateWrapper ptWrapper) {
                executeTemplate(project, virtualFile, ptWrapper);
            }
        }.show();
    }

    private static void executeTemplate(Project project, VirtualFile virtualFile, PackageTemplateWrapper ptWrapper) {
        preExecuteTemplate(project, virtualFile, ptWrapper);

        ptWrapper.prepareGlobals();
        ptWrapper.addGlobalVariablesToFileTemplates();
        ptWrapper.replaceNameVariable();
        ptWrapper.runElementsScript();

        collectAndExecuteActions(project, virtualFile, ptWrapper);
    }

    private static void collectAndExecuteActions(Project project, VirtualFile virtualFile, PackageTemplateWrapper ptWrapper) {
        List<SimpleAction> listSimpleAction = new ArrayList<>();

        ptWrapper.collectSimpleActions(project, virtualFile, listSimpleAction);
        ptWrapper.collectInjectionActions(project, listSimpleAction);

//        TextInjection textInjection = new TextInjection();
//
//        ArrayList<SearchAction> listSearchAction = new ArrayList<>();
//        listSearchAction.add(new SearchAction(SearchActionType.FILE, "Main.java", SearchEngine.DEEP_LIMITLESS, false));
//
//
//        textInjection.setCustomPath(new CustomPath(listSearchAction));
//        textInjection.setDescription("sds");
//        textInjection.setInjectDirection(InjectDirection.BEFORE);
//        textInjection.setRegexp(false);
//        textInjection.setTextToInject("TestText");
//        textInjection.setTextToSearch("MyToken");
//
//        listSimpleAction.add(new InjectTextAction(project, textInjection,ptWrapper.getPackageTemplate().getMapGlobalVars()));

        ActionRequest actionRequest = new ActionRequestBuilder()
                .setProject(project)
                .setActions(listSimpleAction)
                .setActionLabel("Execute PackageTemplate")
                .setAccessPrivileges(AccessPrivileges.WRITE)
                .setConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION)
                .setActionListener(new ActionRequest.ActionFinishListener() {
                    @Override
                    public void onFinish() {
                        if (ptWrapper.getPackageTemplate().shouldShowReport()) {
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
