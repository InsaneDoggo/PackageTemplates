package utils;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import models.InputBlock;
import models.TemplateElement;

import javax.swing.*;

import java.util.ArrayList;

/**
 * Created by Arsen on 15.06.2016.
 */
public class InputManager {

    private JPanel container;
    private ArrayList<InputBlock> listInputBlock;
    private FileTemplateManager fileTemplateManager;

    private int paddingLevel = 0;

    public InputManager(JPanel container) {
        this.container = container;
        fileTemplateManager = FileTemplateManager.getDefaultInstance();
    }

    public void addElement(TemplateElement element){
        if(element.isDirectory()){
            // TODO: 15.06.2016 inc padding
        }

        Logger.log("input for " + (element.isDirectory() ? " dir  " : " file ") +  element.getName() );
    }

    public void onPackageEnds() {
        // TODO: 15.06.2016 add divider
    }

}
