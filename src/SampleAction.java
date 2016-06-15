import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import models.PackageTemplate;
import models.TemplateElement;
import org.jetbrains.annotations.NotNull;
import ui.dialogs.NewPackageDialog;
import utils.Logger;
import utils.StringTools;
import utils.TemplateValidator;

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

        ArrayList<TemplateElement> listElementsInner = new ArrayList<>();
        listElementsInner.add(new TemplateElement(false, "Prost", null));
        listElementsInner.add(new TemplateElement(false, "Slojn", null));

        ArrayList<TemplateElement> listElements = new ArrayList<>();
        listElements.add(new TemplateElement(false, "Prost", null));
        listElements.add(new TemplateElement(true, "Pre${PACKAGE_TEMPLATE_NAME}Post", listElementsInner));
//        listElements.add(new TemplateElement(false, "fake", null));


        PackageTemplate packageTemplate = new PackageTemplate("TestPT", "MegaPT", "tipa description", listElements);

        if(TemplateValidator.isTemplatesValid(packageTemplate)){
            showDialog(e.getProject(), packageTemplate);
        }
    }

    private void showDialog(Project project, PackageTemplate packageTemplate) {
        NewPackageDialog dialog = new NewPackageDialog(project, "New package") {
            @Override
            public void onFinish(String result) {
                Logger.log("onFinish " + result);
                StringTools.replaceNameVariable(packageTemplate, "Ivan");
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
