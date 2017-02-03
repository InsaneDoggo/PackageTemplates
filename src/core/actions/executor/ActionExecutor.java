package core.actions.executor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import core.actions.custom.base.SimpleAction;
import global.utils.Logger;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Created by Arsen on 14.01.2017.
 */
public class ActionExecutor {

    public static boolean runAsTransaction(Project project, List<SimpleAction> actions, String actionLabel, AccessPrivileges accessPrivileges) {
        // Action
        Computable<Boolean> computable = () -> {
            for (SimpleAction action : actions) {
                if (!action.run(null)) {
                    return false;
                }
            }
            return true;
        };

        // Execution
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            switch (accessPrivileges) {
                case NONE:
                    return computable.compute();
                case READ:
                    return ApplicationManager.getApplication().runReadAction(computable);
                case WRITE:
                    return ApplicationManager.getApplication().runWriteAction(computable);
                default:
                    throw new RuntimeException("Unknown AccessPrivileges " + accessPrivileges.name());
            }
        });

        // Handle result
        CommandProcessor.getInstance().executeCommand(project, futureTask, actionLabel, null);
        try {
            return futureTask.get();
        } catch (Exception ex) {
            Logger.log("runAsTransaction: " + ex.getMessage());
            return false;
        }
    }

    public static boolean runAction(Project project, SimpleAction action, String actionLabel, AccessPrivileges accessPrivileges) {
        // Action
        Computable<Boolean> computable = () -> action.run(null);

        // Execution
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            switch (accessPrivileges) {
                case NONE:
                    return computable.compute();
                case READ:
                    return ApplicationManager.getApplication().runReadAction(computable);
                case WRITE:
                    return ApplicationManager.getApplication().runWriteAction(computable);
                default:
                    throw new RuntimeException("Unknown AccessPrivileges " + accessPrivileges.name());
            }
        });

        // Handle result
        CommandProcessor.getInstance().executeCommand(project, futureTask, actionLabel, null);
        try {
            return futureTask.get();
        } catch (Exception ex) {
            Logger.log("run Action: " + ex.getMessage());
            return false;
        }
    }


}
