package core.actions.executor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.util.Computable;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.request.ActionRequest;
import core.errors.ErrorHelper;
import global.utils.Logger;
import global.utils.NotificationHelper;
import global.utils.ProgressHelper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Arsen on 14.01.2017.
 */
public class ActionExecutor {

    public static boolean runAsTransaction(ActionRequest request) {
        // Action
        Computable<Boolean> computable = () -> ProgressHelper.runProcessWithProgress(request.project, new FutureTask<>(() -> {
            ErrorHelper.add("another thread");

            for (SimpleAction action : request.actions) {
                if (!action.run()) {
                    return false;
                }
            }
            return true;
        }));

        // Execution
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            switch (request.accessPrivileges) {
                case NONE:
                    return computable.compute();
                case READ:
                    return ApplicationManager.getApplication().runReadAction(computable);
                case WRITE:
                    return ApplicationManager.getApplication().runWriteAction(computable);
                default:
                    throw new RuntimeException("Unknown AccessPrivileges " + request.accessPrivileges.name());
            }
        });

        // Handle result
        if (request.isUndoable) {
            CommandProcessor.getInstance().executeCommand(request.project, futureTask, request.actionLabel, request.groupId, request.confirmationPolicy);
        } else {
            CommandProcessor.getInstance().runUndoTransparentAction(futureTask);
        }

        Logger.log(ErrorHelper.get());
        NotificationHelper.info(request.actionLabel, "Success!");
        return true;
    }

    public static boolean runAction(ActionRequest request) {
        // Action
        Computable<Boolean> computable = request.actions.get(0)::run;

        // Execution
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            switch (request.accessPrivileges) {
                case NONE:
                    return computable.compute();
                case READ:
                    return ApplicationManager.getApplication().runReadAction(computable);
                case WRITE:
                    return ApplicationManager.getApplication().runWriteAction(computable);
                default:
                    throw new RuntimeException("Unknown AccessPrivileges " + request.accessPrivileges.name());
            }
        });

        // Handle result
        if (request.isUndoable) {
            CommandProcessor.getInstance().executeCommand(request.project, futureTask, request.actionLabel, request.groupId, request.confirmationPolicy);
        } else {
            CommandProcessor.getInstance().runUndoTransparentAction(futureTask);
        }

        try {
            return futureTask.get();
        } catch (Exception ex) {
            Logger.log("run Action: " + ex.getMessage());
            Logger.printStack(ex);
            return false;
        }
    }


}
