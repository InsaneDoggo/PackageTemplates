package core.actions.executor;

import com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import core.actions.custom.DummyDirectoryAction;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.request.ActionRequest;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.PendingActionReport;
import global.utils.NotificationHelper;
import global.utils.ProgressHelper;

/**
 * Created by Arsen on 14.01.2017.
 */
public class ActionExecutor {

    public static void runAsTransaction(ActionRequest request) {
        // Action
        ApplicationManager.getApplication().runWriteAction(() -> {
            Runnable runnable = () -> ProgressHelper.runProcessWithProgress(request.project, () -> {
                ReportHelper.setState(ExecutionState.IN_PROGRESS);

                for (SimpleAction action : request.actions) {
                    if (action instanceof DummyDirectoryAction) {
                        continue;
                    }

                    action.setId(ReportHelper.getGenerateId());
                    ReportHelper.putReport(new PendingActionReport(action));
                }

                for (SimpleAction action : request.actions) {
                    action.run();
                    if (!ReportHelper.shouldContinue()) {
                        preFinish(request);
                        return;
                    }
                }

                ReportHelper.setState(ExecutionState.SUCCESS);
                preFinish(request);
            });

            // execute
            if (request.isUndoable) {
                CommandProcessor.getInstance().executeCommand(request.project, runnable, request.actionLabel, request.groupId, request.confirmationPolicy);
            } else {
                CommandProcessor.getInstance().runUndoTransparentAction(runnable);
            }
        });
    }

    private static void preFinish(ActionRequest request) {
        if (ReportHelper.getState() == ExecutionState.SUCCESS) {
            ApplicationManager.getApplication().invokeLater(() -> {
                NotificationHelper.info(request.actionLabel, "Success!");
                if (request.actionFinishListener != null) {
                    request.actionFinishListener.onFinish();
                }
            });
        } else {
            ApplicationManager.getApplication().invokeLater(() -> {
                NotificationHelper.info(request.actionLabel, "Failed!");
                if (request.actionFinishListener != null) {
                    request.actionFinishListener.onFail();
                }
            });
        }
    }

}
