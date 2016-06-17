import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import models.PackageTemplate;
import models.TemplateElement;
import org.jetbrains.annotations.NotNull;
import ui.dialogs.NewPackageDialog;
import utils.InputManager;
import utils.Logger;
import utils.StringTools;
import utils.TemplateValidator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ArrayList<TemplateElement> listElements = new ArrayList<>();
        ArrayList<TemplateElement> listElementsInner = new ArrayList<>();
        PackageTemplate packageTemplate = new PackageTemplate("TestPT", "MegaPT", "tipa description", listElements);

        TemplateElement element = new TemplateElement(true, "Pre${PACKAGE_TEMPLATE_NAME}Post", listElementsInner, packageTemplate.getTemplateElement());

        listElements.add(new TemplateElement(false, "Prost", null, packageTemplate.getTemplateElement()));
        listElements.add(element);
        listElementsInner.add(new TemplateElement(false, "Prost", null, element));
        listElementsInner.add(new TemplateElement(false, "Slojn", null, element));
//        listElements.add(new TemplateElement(false, "fake", null));


        if(TemplateValidator.isTemplatesValid(packageTemplate)){
            showDialog(e.getProject(), packageTemplate);
        }
    }

    private void showDialog(Project project, PackageTemplate packageTemplate) {
        InputManager inputManager = new InputManager(project, packageTemplate);

        for (TemplateElement element : packageTemplate.getListTemplateElement()){
            element.makeInputBlock(inputManager);
        }

        // TODO: 17.06.2016 build panel | do it in InputManager
//        inputManager.buildPanel();

        NewPackageDialog dialog = new NewPackageDialog(project, "New package from \""+packageTemplate.getName()+"\"", inputManager) {
            @Override
            public void onFinish(String result) {
                Logger.log("onFinish " + result);
                StringTools.replaceNameVariable(packageTemplate, "Ivan");
            }
        };
    }

    private void writeClass(AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file != null && file.isDirectory()) {
            PsiDirectory dir = PsiManager.getInstance(e.getProject()).findDirectory(e.getData(CommonDataKeys.VIRTUAL_FILE));

            PsiClass psiClass = JavaDirectoryService.getInstance().createClass(dir, "MegaFile", "Prost", false, getCustomProperties());
        }
    }

    @NotNull
    private HashMap<String, String> getCustomProperties() {
        HashMap<String, String> map = new HashMap<>();
        map.put("myVar", "\"HelloWorld\"");
        return map;
    }
}
