package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import org.jetbrains.annotations.Nullable;
import wrappers.GlobalVariableWrapper;
import wrappers.PackageTemplateWrapper;
import utils.GridBagFactory;
import utils.TemplateValidator;
import utils.WrappersFactory;

import java.awt.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends BaseDialog {

    public abstract void onSuccess(PackageTemplateWrapper ptWrapper);

    public abstract void onCancel();

    private PackageTemplateWrapper ptWrapper;

    public NewPackageDialog(@Nullable Project project, String title, PackageTemplate packageTemplate) {
        super(project);
        ptWrapper = WrappersFactory.wrapPackageTemplate(project, packageTemplate, PackageTemplateWrapper.ViewMode.USAGE);
        init();
        setTitle(title);
    }

//    public void updateHighlight() {
//        for (InputBlock inputBlock : inputManager.getListInputBlock()) {
//            if(inputBlock.getTfName().getEditor() == null){
//                continue;
//            }
//
//            UIHelper.applyHighlightRange(inputBlock.getTfName().getText(), project, inputBlock.getTfName().getEditor());
//        }
//    }

    @Override
    protected ValidationInfo doValidate() {
        ValidationInfo result;
        if( ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE ) {
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

        return ptWrapper.getRootElement().validateFields();
    }


    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();
        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Override
    public void onOKAction() {
        // save fields
        ptWrapper.collectDataFromFields();
        ptWrapper.replaceNameVariable();
        onSuccess(ptWrapper);
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }
}
