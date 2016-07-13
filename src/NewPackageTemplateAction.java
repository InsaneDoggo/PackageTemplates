import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiDirectory;
import custom.dialogs.ConfigurePackageTemplatesDialog;
import custom.dialogs.NewPackageDialog;
import custom.dialogs.SelectPackageTemplateDialog;
import models.PackageTemplate;
import models.TemplateElement;
import reborn.wrappers.PackageTemplateWrapper;
import state.SaveUtil;
import utils.FileWriter;
import utils.InputManager;

import java.util.ArrayList;

/**
 * Created by Arsen on 13.06.2016.
 */
public class NewPackageTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {

//        ConfigurePackageTemplatesDialog dialog = new ConfigurePackageTemplatesDialog(event.getProject()) {
//            @Override
//            public void onSuccess(PackageTemplate packageTemplate) {
//                SaveUtil.getInstance().getTemplateList().add(packageTemplate);
//                SaveUtil.getInstance().save();
//                System.out.println("onSuccess");
//            }
//
//            @Override
//            public void onFail() {
//                System.out.println("onSuccess");
//            }
//        };


//        ImpexDialog dialog = new ImpexDialog(event.getProject(), "Export Templates") {
//            @Override
//            public void onSuccess() {
//                System.out.println("onSuccess");
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("onCancel");
//            }
//        };

        SaveUtil.getInstance().load();
        SelectPackageTemplateDialog dialog = new SelectPackageTemplateDialog(event.getProject()) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                System.out.println("SelectPackageTemplateDialog onSuccess");
                //showDialog(event, packageTemplate);
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

        NewPackageDialog dialog = new NewPackageDialog(event.getProject(), "New package from \"" + packageTemplate.getName() + "\"", inputManager) {
            @Override
            public void onSuccess() {
                createFiles(event, packageTemplate);
            }

            @Override
            public void onCancel() {
            }
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
