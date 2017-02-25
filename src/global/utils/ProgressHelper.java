package global.utils;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.RunnableBackgroundableWrapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Arsen on 10.10.2016.
 */
public class ProgressHelper {

    public static boolean runProcessWithProgress(Project project, FutureTask<Boolean> runnable) {
        Logger.log("runProcessWithProgress ");
        ProgressWindow progressIndicator = new ProgressWindow(false, project);
        progressIndicator.setIndeterminate(true);

//        return runSync(project, runnable);
        return runAsync(project, runnable, progressIndicator);
    }

    private static boolean runSync(Project project, FutureTask<Boolean> runnable) {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnable, "Creating PackageTemplate", false, project);
        try {
            return runnable.get();
        } catch (InterruptedException | ExecutionException e) {
            Logger.printStack(e);
            return false;
        }
    }

    private static boolean runAsync(Project project, FutureTask<Boolean> runnable, ProgressWindow progressIndicator) {
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                new RunnableBackgroundableWrapper(project, "Executing PackageTemplate", runnable), progressIndicator);

//         Handle result
//        try {
//            return runnable.get();
//        } catch (InterruptedException | ExecutionException e) {
//            Logger.printStack(e);
            return false;
//        }
    }

}
