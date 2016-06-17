package utils;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.panels.HorizontalBox;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBImageIcon;
import models.InputBlock;
import models.PackageTemplate;
import models.TemplateElement;
import org.apache.velocity.runtime.parser.ParseException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.block;

/**
 * Created by Arsen on 15.06.2016.
 */
public class InputManager {

    private int paddingScale = 0;

    private JPanel panel;
    private PackageTemplate packageTemplate;
    private Project project;
    private ArrayList<InputBlock> listInputBlock;
    private FileTemplateManager fileTemplateManager;

    public static final ArrayList<String> listAttributesToRemove = new ArrayList<String>() {{
        add(FileTemplate.ATTRIBUTE_NAME);
        add(FileTemplate.ATTRIBUTE_PACKAGE_NAME);
        add(PackageTemplate.ATTRIBUTE_PACKAGE_TEMPLATE_NAME);
    }};

    public InputManager(Project project, PackageTemplate packageTemplate) {
        this.project = project;
        this.packageTemplate =  packageTemplate;

        listInputBlock = new ArrayList<>();
        fileTemplateManager = FileTemplateManager.getDefaultInstance();
        initPanel();
    }

    public ArrayList<InputBlock> getListInputBlock() {
        return listInputBlock;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//        HorizontalBox hBox = new HorizontalBox();
//        VerticalBox vBox = new VerticalBox();
//        vBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//        hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        JLabel jLabel_1 = new JLabel("Var 1");
//        JLabel jLabel_2 = new JLabel("Var 2");
//        JLabel jLabel_3 = new JLabel("Var 3");
//        JLabel jLabel_4 = new JLabel("Var 4");
//        JLabel jLabel_5 = new JLabel("Var 5");
//        JLabel jLabel_6 = new JLabel("Var 6");
//
//        hBox.add(jLabel_1);
//        hBox.add(jLabel_2);
//        hBox.add(jLabel_3);
//        vBox.add(jLabel_4);
//        vBox.add(jLabel_5);
//        vBox.add(jLabel_6);
//
//        panel.add(hBox);
//        panel.add(vBox);

        HorizontalBox hBox = getHeader();

        paddingScale++;
        panel.add(hBox);
    }

    @NotNull
    private HorizontalBox getHeader() {
        InputBlock inputBlock = new InputBlock(packageTemplate.getTemplateElement(), paddingScale, null);
        listInputBlock.add(inputBlock);

        HorizontalBox hBox = new HorizontalBox();
        hBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jLabel = new JLabel(AllIcons.Nodes.Package);
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel.setText(PackageTemplate.ATTRIBUTE_PACKAGE_TEMPLATE_NAME);

        hBox.add(jLabel);
        hBox.add(inputBlock.getTfName());
        return hBox;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void addElement(TemplateElement element) {
        if (element.isDirectory()) {
            addDirectory(element);
        } else {
            addClass(element);
        }
        Logger.log("input for " + (element.isDirectory() ? " dir  " : " file ") + element.getName());
    }

    private void addClass(TemplateElement element) {
        FileTemplate fileTemplate = fileTemplateManager.getTemplate(element.getName());
        if (fileTemplate != null) {
            InputBlock inputBlock = new InputBlock(element, paddingScale, getUnsetAttrs(fileTemplate));
//            getClassPanel()
            listInputBlock.add(inputBlock);
        }
    }

    private void addDirectory(TemplateElement element) {
        // TODO: 17.06.2016 add separator
        listInputBlock.add(new InputBlock(element, paddingScale, null));

        // panel. add

        paddingScale++;
    }


    private String[] getUnsetAttrs(FileTemplate fileTemplate) {
        try {
            return getWithoutDefaultAttributes(
                    fileTemplate.getUnsetAttributes(new Properties(), project)
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] getWithoutDefaultAttributes(String[] unsetAttributes) {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(unsetAttributes));
        Iterator<String> iterator =  list.iterator();
        while(iterator.hasNext()){
            if( listAttributesToRemove.contains(iterator.next()) ){
                iterator.remove();
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void onPackageEnds() {
        paddingScale--;
    }

    public HorizontalBox getClassPanel(InputBlock inputBlock) {
        HorizontalBox hBox = new HorizontalBox();
        hBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jLabel = new JLabel(AllIcons.Nodes.Class, SwingConstants.LEFT);

        hBox.add(jLabel);
        hBox.add(inputBlock.getTfName());

        return hBox;
    }

    public void buildPanel() {
        //add class field
//        block.getTfName()
        //add vars pane
    }
}
