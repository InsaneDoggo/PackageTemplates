package core.actions.generated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.NewPackageTemplateAction;
import global.models.PackageTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 11.09.2016.
 */
public class RunTemplateAction extends BaseAction {

    private PackageTemplate packageTemplate;

    public RunTemplateAction(@Nullable String name, PackageTemplate packageTemplate) {
        super(name);
        this.packageTemplate = packageTemplate;
    }


    @Override
    public void actionPerformed(AnActionEvent event) {
        VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        Project project = event.getProject();

        if (packageTemplate.isSkipDefiningNames()) {
            NewPackageTemplateAction.executeTemplateSilently(packageTemplate, project, virtualFile);
        } else {
            NewPackageTemplateAction.showDialog(packageTemplate, project, virtualFile);
        }

    }

}
