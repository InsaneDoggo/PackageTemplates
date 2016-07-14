package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.util.ui.GridBag;
import models.InputBlock;
import models.PackageTemplate;
import org.jetbrains.annotations.Nullable;
import reborn.wrappers.GlobalVariableWrapper;
import reborn.wrappers.PackageTemplateWrapper;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Properties;

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
//            UIMaker.applyHighlightRange(inputBlock.getTfName().getText(), project, inputBlock.getTfName().getEditor());
//        }
//    }

//    private void saveVariablesFromDialog() {
//        for (InputBlock block : inputManager.getListInputBlock()) {
//            if(block.isGlobalVariable()){
//                continue;
//            }
//
//            block.getElement().setName(block.getTfName().getText());
//
//            if (!block.getElement().isDirectory()) {
//                if (block.getPanelVariables() != null) {
//                    Properties properties = new Properties();
//                    properties = block.getPanelVariables().getProperties(properties);
//
//                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
//                        block.getElement().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());
//                    }
//                }
//                //add GLOBALS
//                block.getElement().getMapProperties().putAll(inputManager.getMapGlobalProperties());
//            }
//        }
//    }

    @Override
    protected ValidationInfo doValidate() {
        ValidationInfo result;
        if( ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE ) {
            result = TemplateValidator.validateTextField(ptWrapper.etfName, TemplateValidator.FieldType.PACKAGE_TEMPLATE_NAME);
            if (result != null) {
                return result;
            }
            result = TemplateValidator.validateTextField(ptWrapper.etfDescription, TemplateValidator.FieldType.PLAIN_TEXT);
            if (result != null) {
                return result;
            }
        }

        for (GlobalVariableWrapper gvWrapper : ptWrapper.getListGlobalVariableWrapper()) {
            result = TemplateValidator.validateTextField(gvWrapper.getTfValue(), TemplateValidator.FieldType.GLOBAL_VARIABLE);
            if (result != null) {
                return result;
            }
        }

        return ptWrapper.getRootElement().validateFields();
    }


    @Override
    void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();
        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Override
    void onOKAction() {
        // save fields
        ptWrapper.collectDataFromFields();
        onSuccess(ptWrapper);
    }

    @Override
    void onCancelAction() {
        onCancel();
    }
}
