package core.actions.newPackageTemplate.dialogs.implement;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import global.models.PackageTemplate;
import global.utils.TemplateValidator;
import global.utils.WrappersFactory;
import global.wrappers.PackageTemplateWrapper;

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
        result = TemplateValidator.validateProperties(ptWrapper);
        if (result != null) return result;

        result = TemplateValidator.validateGlobalVariables(ptWrapper);
        if (result != null) return result;

        result = ptWrapper.getRootElement().validateFields();
        if (result != null) return result;

        prepareFields();

        return TemplateValidator.checkExisting(ptWrapper, virtualFile, project);
    }

    private void prepareFields() {
        ptWrapper.collectDataFromFields();
        ptWrapper.prepareGlobals();
        ptWrapper.addGlobalVariablesToFileTemplates();
        ptWrapper.replaceNameVariable();
        ptWrapper.runElementsGroovyScript();
    }

}
