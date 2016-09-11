package core.actions.generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.NewPackageTemplateAction;
import global.models.PackageTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 11.09.2016.
 */
public class BaseAction extends AnAction {

    private String name;
    private PackageTemplate packageTemplate;

    public BaseAction(@Nullable String name, PackageTemplate packageTemplate) {
        super(name);
        this.name = name;
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

    public String getName() {
        return name;
    }
}
