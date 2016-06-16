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

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.title;
import static java.io.File.separator;

/**
 * Created by Arsen on 15.06.2016.
 */
public class InputManager {

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
            paddingScale++;
            //separator + package title
            listInputBlock.add(
                    new InputBlock(element,
                            UIMaker.getLabel("Package Name", paddingScale),
                            UIMaker.getEditorTextField("", paddingScale)
                    ));

        } else {
            //for each fileTempVars vars
            FileTemplate fileTemplate = fileTemplateManager.getTemplate(element.getName());
            if (fileTemplate != null) {
                try {
                    for (String property : Arrays.asList(fileTemplate.getUnsetAttributes(new Properties(), project))) {
                        listInputBlock.add(
                                new InputBlock(element,
                                        UIMaker.getLabel(property, paddingScale),
                                        UIMaker.getEditorTextField("", paddingScale)
                                ));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //listInputBlock.add(block);
        }

        Logger.log("input for " + (element.isDirectory() ? " dir  " : " file ") + element.getName());
    }

    public void onPackageEnds() {
        paddingScale--;
    }

}
