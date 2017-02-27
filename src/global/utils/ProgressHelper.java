package global.utils;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.RunnableBackgroundableWrapper;

/**
 * Created by Arsen on 10.10.2016.
 */
public class ProgressHelper {

    public static void runProcessWithProgress(Project project, Runnable runnable) {
        ProgressWindow progressIndicator = new ProgressWindow(false, project);
        progressIndicator.setIndeterminate(true);

        runSync(project, runnable);
//        runAsync(project, runnable, progressIndicator);
    }

    private static void runSync(Project project,  Runnable runnable) {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnable, "Creating PackageTemplate", false, project);
    }

    private static void runAsync(Project project, Runnable runnable, ProgressWindow progressIndicator) {
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                new RunnableBackgroundableWrapper(project, "Executing PackageTemplate", runnable), progressIndicator);
    }

}
