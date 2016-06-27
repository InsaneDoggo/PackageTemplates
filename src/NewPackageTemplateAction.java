import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiDirectory;
import custom.dialogs.ConfigurePackageTemplatesDialog;
import custom.dialogs.SelectFileTemplateDialog;
import custom.dialogs.SelectPackageTemplateDialog;
import io.SaveUtil;
import models.PackageTemplate;
import models.TemplateElement;
import custom.dialogs.NewPackageDialog;
import utils.FileWriter;
import utils.InputManager;
import utils.Logger;

import java.util.ArrayList;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    SaveUtil saveUtil;

    @Override
    public void actionPerformed(AnActionEvent event) {
        ArrayList<TemplateElement> listElements = new ArrayList<>();
        ArrayList<TemplateElement> listElementsInner = new ArrayList<>();
        PackageTemplate packageTemplate = new PackageTemplate("TestPT", "tipa description", listElements);

        TemplateElement element = new TemplateElement("Pre${PACKAGE_TEMPLATE_NAME}Post", listElementsInner, packageTemplate.getTemplateElement());

        listElements.add(new TemplateElement("Prost", "Prost", "java", packageTemplate.getTemplateElement()));
        listElements.add(element);
        listElementsInner.add(new TemplateElement("Prost", "Prost", "java", element));
        listElementsInner.add(new TemplateElement("Slojn", "Slojn", "java", element));


//        if (TemplateValidator.isTemplatesValid(packageTemplate)) {
//            showDialog(event, packageTemplate);
//        }


        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                System.out.println("onSuccess");
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }
        };
        dialog.show();


//        SelectFileTemplateDialog dialog = new SelectFileTemplateDialog(event.getProject()) {
//            @Override
//            public void onSuccess(FileTemplate fileTemplate) {
//                System.out.println("fileTemplate " + fileTemplate.getName());
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("onCancel");
//            }
//        };
//        dialog.show();

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
                Logger.log("onSuccess " + result);
            }
        };
//        dialog.updateHighlight();
        dialog.show();
    }

    private void createFiles(AnActionEvent event, PackageTemplate packageTemplate) {
        PsiDirectory currentDir = FileWriter.findCurrentDirectory(event);
        if (currentDir != null) {
            packageTemplate.getTemplateElement().writeFile(currentDir, event.getProject());
        }
    }

}
