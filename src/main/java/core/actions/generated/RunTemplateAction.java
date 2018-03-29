package core.actions.generated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.NewPackageTemplateAction;
import global.models.PackageTemplate;
import global.utils.factories.GsonFactory;
import global.utils.Logger;
import global.utils.templates.PackageTemplateHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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

        PackageTemplate pt = getPackageTemplateByName(packageTemplate.getName());
        if(pt == null){
            // TODO: 29.09.2016 template not found msg
            Logger.log("action: pt == null");
            return;
        }
        packageTemplate = GsonFactory.cloneObject(pt, PackageTemplate.class);

        if (packageTemplate.isSkipDefiningNames()) {
            NewPackageTemplateAction.executeTemplateSilently(packageTemplate, project, virtualFile);
        } else {
            NewPackageTemplateAction.showDialog(packageTemplate, project, virtualFile);
        }

    }

    @Nullable
    private PackageTemplate getPackageTemplateByName(String name) {
        ArrayList<PackageTemplate> list = PackageTemplateHelper.getListPackageTemplate();

        for(PackageTemplate pt : list){
            if(pt.getName().equals(name)){
                return pt;
            }
        }
        return null;
    }

}
