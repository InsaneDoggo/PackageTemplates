import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ui.dialogs.NewPackageDialog;
import utils.Logger;
import utils.TemplatesValidator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 13.06.2016.
 */
public class SampleAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
//        FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
//        FileTemplate[] templates = fileTemplateManager.getAllTemplates();
//
//        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
//
//        if (file != null && file.isDirectory()) {
//            PsiDirectory dir = PsiManager.getInstance(e.getProject()).findDirectory(e.getData(CommonDataKeys.VIRTUAL_FILE));
//
//            PsiClass psiClass = JavaDirectoryService.getInstance().createClass(dir, "MegaFile", "Prost", false, getCustomProperties());
//        }

        ArrayList<String> templateNames = new ArrayList<>();
        templateNames.add("Prost");
        templateNames.add("Slojn");

        if(TemplatesValidator.isTemplatesValid(templateNames)){
            showDialog(e.getProject());
        }
    }

    private void showDialog(Project project) {
        NewPackageDialog dialog = new NewPackageDialog(project, "My Dialog") {
            @Override
            public void onFinish(String result) {
                Logger.log("onFinish " + result);
            }
        };
    }

    @NotNull
    private HashMap<String, String> getCustomProperties() {
        HashMap<String, String> map = new HashMap<>();
        map.put("myVar", "\"HelloWorld\"");
        return map;
    }
}
