package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.undoable.TestDeleteFileAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.executor.request.ActionRequest;
import core.actions.executor.request.ActionRequestBuilder;
import core.library.ui.LibraryDialog;
import core.report.ReportHelper;
import core.settings.SettingsDialog;
import global.utils.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Arsen on 28.07.2017.
 */
public class TestFeature {

    private Project project;
    private AnActionEvent event;
    private VirtualFile virtualFile;

    public TestFeature(Project project, AnActionEvent event, VirtualFile virtualFile) {
        this.project = project;
        this.event = event;
        this.virtualFile = virtualFile;

//        runTest();
//        runSettingsTest();
        runTabsTest();
    }


    //=================================================================
    //  Settings TEST
    //=================================================================
    private void runTabsTest() {
        LibraryDialog dialog = new LibraryDialog(project);
        dialog.show();
    }


    //=================================================================
    //  Settings TEST
    //=================================================================
    private void runSettingsTest() {
        SettingsDialog dialog = new SettingsDialog(project);
        dialog.show();
    }


    //=================================================================
    //  Feature
    //=================================================================
    private void runTest() {
        ArrayList<SimpleAction> simpleActions = new ArrayList<>();
        TestDeleteFileAction action = new TestDeleteFileAction(project, new File(virtualFile.getPath()));
        simpleActions.add(action);

        ActionRequest actionRequest = new ActionRequestBuilder()
                .setProject(project)
                .setActions(simpleActions)
                .setActionLabel("Test Feature Action")
                .setAccessPrivileges(AccessPrivileges.WRITE)
                .setUndoable(true)
                .setConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION)
                .setActionListener(new ActionRequest.ActionFinishListener() {
                    @Override
                    public void onFinish() {
                        Logger.log("onFinish");
                    }

                    @Override
                    public void onFail() {
                        Logger.log("onFail");
                    }
                })
                .build();

        ActionExecutor.runAsTransaction(actionRequest);

//        String commandId = "testId";
//        CommandProcessor.getInstance().executeCommand(project, () ->
//                        ApplicationManager.getApplication().runWriteAction(this::execute),
//                commandId, commandId);
    }

    private void execute() {
        TestDeleteFileAction action = new TestDeleteFileAction(project, new File(virtualFile.getPath()));
        action.run();
    }

}
