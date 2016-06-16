package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import models.InputBlock;
import models.TemplateElement;
import org.apache.velocity.runtime.parser.ParseException;

import javax.swing.*;
import javax.swing.border.Border;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Arsen on 15.06.2016.
 */
public class InputManager {

    public static final int DEFAULT_PADDING = 0;
    public static final int PADDING = 8;

    private int paddingScale = 0;

    private JPanel container;
    private Project project;
    private ArrayList<InputBlock> listInputBlock;
    private FileTemplateManager fileTemplateManager;


    public InputManager(JPanel container, Project project) {
        this.container = container;
        this.project = project;
        fileTemplateManager = FileTemplateManager.getDefaultInstance();
    }

    public void addElement(TemplateElement element) {
        if (element.isDirectory()) {
            // TODO: 15.06.2016 inc padding
        } else {
            //for each fileTempVars vars
            FileTemplate fileTemplate = fileTemplateManager.getTemplate(element.getName());
            if (fileTemplate != null) {
                try {
                    for (String property : Arrays.asList(fileTemplate.getUnsetAttributes(new Properties(), project))) {
                        listInputBlock.add(new InputBlock(element, getLabel(property), getEditorTextField("")));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //listInputBlock.add(block);
        }

        Logger.log("input for " + (element.isDirectory() ? " dir  " : " file ") + element.getName());
    }

    private EditorTextField getEditorTextField(String defValue) {
        EditorTextField etfName = new EditorTextField();
        etfName.setText(defValue);
        setLeftPadding(etfName, PADDING * paddingScale);
        return etfName;
    }

    private JLabel getLabel(String property) {
        JLabel label = new JLabel(property);
        return label;
    }

    public void onPackageEnds() {
        // TODO: 15.06.2016 dec padding
    }

    private void setLeftPadding(JComponent component, int padding) {
        //Border used as padding
        component.setBorder(BorderFactory.createEmptyBorder(padding, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING));
    }

}
