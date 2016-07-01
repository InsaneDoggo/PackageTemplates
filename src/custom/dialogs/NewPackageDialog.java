package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import models.InputBlock;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;
import utils.StringTools;
import utils.UIMaker;

import javax.swing.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends DialogWrapper {

    public abstract void onSuccess();
    public abstract void onCancel();

    JPanel panel;
    InputManager inputManager;

    public NewPackageDialog(@Nullable Project project, String title, InputManager inputManager) {
        super(project);
        this.inputManager = inputManager;
        this.panel = inputManager.getPanel();
        init();
        setTitle(title);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case NewPackageDialog.OK_EXIT_CODE:
                inputManager.collectGlobalVars();
                saveVariablesFromDialog();
                inputManager.getPackageTemplate().replaceNameVariable(inputManager);
                onSuccess();
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onCancel();
                break;
        }
    }

    public void updateHighlight() {
        for (InputBlock inputBlock : inputManager.getListInputBlock()) {
            if(inputBlock.getTfName().getEditor() == null){
                continue;
            }

            UIMaker.applyHighlightRange(inputBlock.getTfName().getText(), inputManager.getProject(), inputBlock.getTfName().getEditor());
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
                    //add GLOBALS
                    block.getElement().getMapProperties().putAll(inputManager.getMapGlobalProperties());
                }
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

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

}
