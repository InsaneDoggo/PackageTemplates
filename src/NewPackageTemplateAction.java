import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiDirectory;
import models.PackageTemplate;
import models.TemplateElement;
import ui.dialogs.NewPackageDialog;
import utils.FileWriter;
import utils.InputManager;
import utils.Logger;
import utils.TemplateValidator;

import java.util.ArrayList;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ArrayList<TemplateElement> listElements = new ArrayList<>();
        ArrayList<TemplateElement> listElementsInner = new ArrayList<>();
        PackageTemplate packageTemplate = new PackageTemplate("TestPT", "MegaPT", "tipa description", listElements);

        TemplateElement element = new TemplateElement(true, "Pre${PACKAGE_TEMPLATE_NAME}Post", null, listElementsInner, packageTemplate.getTemplateElement());

        listElements.add(new TemplateElement(false, "Prost", "Prost", null, packageTemplate.getTemplateElement()));
        listElements.add(element);
        listElementsInner.add(new TemplateElement(false, "Prost", "Prost", null, element));
        listElementsInner.add(new TemplateElement(false, "Slojn", "Slojn", null, element));
//        listElements.add(new TemplateElement(false, "fake", null));


        if (TemplateValidator.isTemplatesValid(packageTemplate)) {
            showDialog(e, packageTemplate);
        }
    }

    private void showDialog(AnActionEvent event, PackageTemplate packageTemplate) {
        InputManager inputManager = new InputManager(event, packageTemplate);

        for (TemplateElement element : packageTemplate.getListTemplateElement()) {
            element.makeInputBlock(inputManager);
        }

        NewPackageDialog dialog = new NewPackageDialog(event.getProject(), "New package from \"" + packageTemplate.getName() + "\"", inputManager) {
            @Override
            public void onFinish(String result) {
                //StringTools.replaceNameVariable(packageTemplate, "Ivan");
                createFiles(event, packageTemplate);
                Logger.log("onFinish " + result);
            }
        };
    }

    private void createFiles(AnActionEvent event, PackageTemplate packageTemplate) {
        PsiDirectory currentDir = FileWriter.findCurrentDirectory(event);
        if( currentDir != null ) {
            packageTemplate.getTemplateElement().writeFile(currentDir, event.getProject());
        }
    }

}
