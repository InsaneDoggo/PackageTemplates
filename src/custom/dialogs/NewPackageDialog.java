package custom.dialogs;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import org.jetbrains.annotations.Nullable;
import utils.*;
import wrappers.GlobalVariableWrapper;
import wrappers.PackageTemplateWrapper;

import java.awt.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends BaseDialog {

    public abstract void onSuccess(PackageTemplateWrapper ptWrapper);

    public abstract void onCancel();

    private PackageTemplateWrapper ptWrapper;
    AnActionEvent event;


    public NewPackageDialog(AnActionEvent event, String title, PackageTemplate packageTemplate) {
        super(event.getProject());
        this.event = event;
        ptWrapper = WrappersFactory.wrapPackageTemplate(event.getProject(), packageTemplate, PackageTemplateWrapper.ViewMode.USAGE);
        init();
        setTitle(title);
    }

    @Override
    protected ValidationInfo doValidate() {
        ValidationInfo result;
        if (ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            result = TemplateValidator.validateText(ptWrapper.etfName, ptWrapper.etfName.getText(), TemplateValidator.FieldType.PACKAGE_TEMPLATE_NAME);
            if (result != null) {
                return result;
            }
            result = TemplateValidator.validateText(ptWrapper.etfDescription, ptWrapper.etfDescription.getText(), TemplateValidator.FieldType.PLAIN_TEXT);
            if (result != null) {
                return result;
            }
        }

        for (GlobalVariableWrapper gvWrapper : ptWrapper.getListGlobalVariableWrapper()) {
            result = TemplateValidator.validateText(gvWrapper.getTfValue(), gvWrapper.getTfValue().getText(), TemplateValidator.FieldType.GLOBAL_VARIABLE);
            if (result != null) {
                return result;
            }
        }

        result = ptWrapper.getRootElement().validateFields();
        if (result != null) {
            return result;
        }

        // save fields
        ptWrapper.collectDataFromFields();
        ptWrapper.replaceNameVariable();
        ptWrapper.runElementsGroovyScript();

        PsiDirectory currentDir = FileWriter.findCurrentDirectory(event);
        if (currentDir != null) {
            VirtualFile existingFile = currentDir.getVirtualFile().findChild(ptWrapper.getRootElement().getDirectory().getName());
            if (existingFile != null) {
                return new ValidationInfo(String.format(Localizer.get("DirectoryAlreadyExist"), ptWrapper.getRootElement().getDirectory().getName()));
            }
        }

        return null;
    }


    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();
        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Override
    public void onOKAction() {
        onSuccess(ptWrapper);
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }
}
