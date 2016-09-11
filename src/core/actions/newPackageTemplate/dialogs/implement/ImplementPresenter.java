package core.actions.newPackageTemplate.dialogs.implement;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import global.models.PackageTemplate;
import global.utils.FileWriter;
import global.utils.Localizer;
import global.utils.TemplateValidator;
import global.utils.WrappersFactory;
import global.wrappers.GlobalVariableWrapper;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 11.09.2016.
 */
public class ImplementPresenter implements IFaceImplementPresenter {

    private PackageTemplateWrapper ptWrapper;
    private VirtualFile virtualFile;
    private Project project;
    private ImplementView view;

    public ImplementPresenter(ImplementView view, PackageTemplate pt, VirtualFile virtualFile, Project project) {
        this.view = view;
        this.virtualFile = virtualFile;
        this.ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.USAGE);
        this.project = project;
    }

    @Override
    public void onPreShow() {
        view.buildView(ptWrapper);
    }

    @Override
    public void onOKAction() {
        view.onSuccess(ptWrapper);
    }

    @Override
    public void onCancelAction() {
        view.onCancel();
    }

    @Override
    public ValidationInfo doValidate() {
        ValidationInfo result;
        result = TemplateValidator.validatePackageTemplateProperties(ptWrapper);
        if (result != null) return result;

        result = TemplateValidator.validateGlobalVariables(ptWrapper);
        if (result != null) return result;

        result = ptWrapper.getRootElement().validateFields();
        if(result != null) return result;

        saveFields();

        PsiDirectory currentDir = FileWriter.findCurrentDirectory(project, virtualFile);
        if (currentDir != null) {
            VirtualFile existingFile = currentDir.getVirtualFile().findChild(ptWrapper.getRootElement().getDirectory().getName());
            if (existingFile != null) {
                return new ValidationInfo(String.format(Localizer.get("DirectoryAlreadyExist"), ptWrapper.getRootElement().getDirectory().getName()));
            }
        }

        return null;
    }

    private void saveFields() {
        // save fields
        ptWrapper.collectDataFromFields();
        ptWrapper.replaceNameVariable();
        ptWrapper.runElementsGroovyScript();
    }

}
