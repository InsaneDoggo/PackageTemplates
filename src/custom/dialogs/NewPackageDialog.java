package custom.dialogs;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import models.InputBlock;
import models.TextRange;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;
import utils.StringTools;
import utils.UIMaker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends DialogWrapper {

    public abstract void onFinish(String result);

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
                inputManager.initGlobalProperties();
                saveVariablesFromDialog();
                inputManager.getPackageTemplate().replaceNameVariable(inputManager);
                onFinish("OK_EXIT_CODE");
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onFinish("CANCEL_EXIT_CODE");
                break;
        }
    }

    public void updateHighlight() {
        for (InputBlock inputBlock : inputManager.getListInputBlock()) {
            if(inputBlock.getTfName().getEditor() == null){
                continue;
            }

            TextAttributes attributes = new TextAttributes();
            attributes.setForegroundColor(UIMaker.COLOR_VARIABLES);
            ArrayList<TextRange> list = StringTools.findVariable(inputBlock.getTfName().getText());
            for( TextRange textRange:  list ) {
                HighlightManager.getInstance(inputManager.getProject()).addRangeHighlight(
                        inputBlock.getTfName().getEditor(), textRange.getBegin(), textRange.getEnd(), attributes, true, null );
            }
        }
    }

    private void saveVariablesFromDialog() {
        for (InputBlock block : inputManager.getListInputBlock()) {
            block.getElement().setName(block.getTfName().getText());

            if (!block.getElement().isDirectory()) {
                if (block.getPanelVariables() != null) {
                    Properties properties = new Properties();
                    properties = block.getPanelVariables().getProperties(properties);

                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        block.getElement().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());

                        //add GLOBALS
                        block.getElement().getMapProperties().putAll(inputManager.getMapGlobalProperties());
                    }
                }
            }
        }
    }

    @Override
    protected void doOKAction() {
        if (isInputValid()) {
            super.doOKAction();
        } else {
            // TODO: 19.06.2016 print wrong input
        }
    }

    private boolean isInputValid() {
        // TODO: 19.06.2016 check input
        for (InputBlock block : inputManager.getListInputBlock()) {
            if( block.getTfName().getText().trim().isEmpty() ){
                // TODO: 21.06.2016 print pls fill fields
                    return false;
            }
            if( !StringTools.isNameValid(block.getTfName().getText()) ){
                // TODO: 21.06.2016 print invalid name
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

}
