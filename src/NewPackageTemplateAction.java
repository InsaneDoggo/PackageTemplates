import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiDirectory;
import custom.dialogs.SelectPackageTemplateDialog;
import io.SaveUtil;
import models.PackageTemplate;
import models.TemplateElement;
import custom.dialogs.NewPackageDialog;
import utils.FileWriter;
import utils.InputManager;
import utils.Logger;
import utils.TemplateValidator;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
//        PackageTemplate ptFake = fakePT();
        SaveUtil.getInstance().load();

        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                System.out.println("SelectPackageTemplateDialog onSuccess");
                showDialog(event, packageTemplate);
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }
        };
        dialog.show();
    }

    private PackageTemplate fakePT() {
        ArrayList<TemplateElement> listElements = new ArrayList<>();
        ArrayList<TemplateElement> listElementsInner = new ArrayList<>();
        PackageTemplate packageTemplate = new PackageTemplate("TestPT", "tipa description", listElements);

        TemplateElement element = new TemplateElement("Pre${PACKAGE_TEMPLATE_NAME}Post", listElementsInner, packageTemplate.getTemplateElement());

        listElements.add(new TemplateElement("Prost", "Prost", "java", packageTemplate.getTemplateElement()));
        listElements.add(element);
        listElementsInner.add(new TemplateElement("Prost", "Prost", "java", element));
        listElementsInner.add(new TemplateElement("Slojn", "Slojn", "java", element));
        return packageTemplate;
    }

    private void showDialog(AnActionEvent event, PackageTemplate packageTemplate) {
        InputManager inputManager = new InputManager(event, packageTemplate);

            packageTemplate.getTemplateElement().makeInputBlock(inputManager);
//        for (TemplateElement element : packageTemplate.getListTemplateElement()) {
//            element.makeInputBlock(inputManager);
//        }

        NewPackageDialog dialog = new NewPackageDialog(event.getProject(), "New package from \"" + packageTemplate.getName() + "\"", inputManager) {
            @Override
            public void onSuccess() {
                createFiles(event, packageTemplate);
            }

            @Override
            public void onCancel() {}
        };
        dialog.updateHighlight();
        dialog.show();
    }

    private void createFiles(AnActionEvent event, PackageTemplate packageTemplate) {
        PsiDirectory currentDir = FileWriter.findCurrentDirectory(event);
        if (currentDir != null) {
            packageTemplate.getTemplateElement().writeFile(currentDir, event.getProject());
        }
    }

}
