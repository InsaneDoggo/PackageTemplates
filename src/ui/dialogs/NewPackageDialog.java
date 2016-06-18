package ui.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import models.InputBlock;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;

import javax.swing.*;
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
        show();

        switch (getExitCode()) {
            case NewPackageDialog.OK_EXIT_CODE:
                //todo replace name vars
                inputManager.initGlobalProperties();
                saveVariablesFromDialog(inputManager);
                onFinish("OK_EXIT_CODE");
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onFinish("CANCEL_EXIT_CODE");
                break;
        }
    }

    private void saveVariablesFromDialog(InputManager inputManager) {
        for( InputBlock block : inputManager.getListInputBlock()){
            block.getElement().setName(block.getTfName().getText());

            if( !block.getElement().isDirectory() ){
                if( block.getPanelVariables() != null ) {
                    Properties properties = new Properties();
                    properties = block.getPanelVariables().getProperties(properties);

                    System.out.println("--------- " + block.getElement().getName() +" ----------------" );

                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        System.out.println( entry.getKey()+"  "+entry.getValue() );
                        block.getElement().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());
                        //add def properties
                        //add package name
                        block.getElement().getMapProperties().put(FileTemplate.ATTRIBUTE_PACKAGE_NAME, block.getElement().getParent().getName());
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

}
