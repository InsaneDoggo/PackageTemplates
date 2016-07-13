package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import models.InputBlock;
import models.PackageTemplate;
import org.jetbrains.annotations.Nullable;
import reborn.wrappers.PackageTemplateWrapper;
import utils.InputManager;
import utils.StringTools;
import utils.UIMaker;
import utils.WrappersFactory;

import javax.swing.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends BaseDialog {

    public abstract void onSuccess();
    public abstract void onCancel();

    PackageTemplateWrapper ptWrapper;

    public NewPackageDialog(@Nullable Project project, String title, PackageTemplate packageTemplate) {
        super(project);
        ptWrapper = WrappersFactory.wrapPackageTemplate(project, packageTemplate, PackageTemplateWrapper.ViewMode.USAGE);
        init();
        setTitle(title);
    }

    public void updateHighlight() {
        for (InputBlock inputBlock : inputManager.getListInputBlock()) {
            if(inputBlock.getTfName().getEditor() == null){
                continue;
            }

            UIMaker.applyHighlightRange(inputBlock.getTfName().getText(), project, inputBlock.getTfName().getEditor());
        }
    }

    private void saveVariablesFromDialog() {
        for (InputBlock block : inputManager.getListInputBlock()) {
            if(block.isGlobalVariable()){
                continue;
            }

            block.getElement().setName(block.getTfName().getText());

            if (!block.getElement().isDirectory()) {
                if (block.getPanelVariables() != null) {
                    Properties properties = new Properties();
                    properties = block.getPanelVariables().getProperties(properties);

                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        block.getElement().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                //add GLOBALS
                block.getElement().getMapProperties().putAll(inputManager.getMapGlobalProperties());
            }
        }
    }

    @Override
    protected ValidationInfo doValidate() {
        for (InputBlock block : inputManager.getListInputBlock()) {
            if( block.getTfName().getText().trim().isEmpty() ){
                return new ValidationInfo("Fill empty fields", block.getTfName());
            }
            if( !StringTools.isNameValid(block.getTfName().getText()) ){
                return new ValidationInfo("Name contains illegal symbols", block.getTfName() );
            }
        }
        return null;
    }

}
