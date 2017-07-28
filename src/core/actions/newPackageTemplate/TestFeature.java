package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

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

        runTest();
    }


    //=================================================================
    //  Feature
    //=================================================================
    private void runTest() {
        String commandId = "testId";
        CommandProcessor.getInstance().executeCommand(project, this::execute, commandId, commandId);
    }

    private void execute() {

    }

}
