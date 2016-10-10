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
        ProgressWindow progressIndicator = new ProgressWindow(true, project);
        progressIndicator.setIndeterminate(true);

//        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnable, "Creating PackageTemplate", false, project);
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(
                new RunnableBackgroundableWrapper(project, "Creating PackageTemplate", runnable), progressIndicator);
    }
}
