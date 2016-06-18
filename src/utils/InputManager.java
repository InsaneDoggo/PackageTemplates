package utils;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplatePanel;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import models.InputBlock;
import models.PackageTemplate;
import models.TemplateElement;
import org.apache.velocity.runtime.parser.ParseException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static utils.UIMaker.getClassPanel;

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
    private HashMap<String, String> mapGlobalProperties;

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
        panel.setMinimumSize(new Dimension(UIMaker.DIALOG_MIN_WIDTH  , panel.getPreferredSize().height));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addHeader(panel);

        paddingScale++;
    }

    @NotNull
    private void addHeader(JPanel panel) {
        InputBlock inputBlock = new InputBlock(packageTemplate.getTemplateElement(), paddingScale, null);
        inputBlock.setGlobalVariable(true);
        inputBlock.setGlobalKey(PackageTemplate.ATTRIBUTE_PACKAGE_TEMPLATE_NAME);
        listInputBlock.add(inputBlock);

        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());

        GridBag bag = UIMaker.getDefaultGridBag();

        JLabel jLabel = new JLabel(AllIcons.Nodes.Package);
        jLabel.setText(StringTools.formatConst(PackageTemplate.ATTRIBUTE_PACKAGE_TEMPLATE_NAME));

        container.add(jLabel, bag.nextLine().next());
        container.add(inputBlock.getTfName(), bag.next());

        panel.add(container);
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
            panel.add(UIMaker.getClassPanel(inputBlock, paddingScale));
            // add variables
            JComponent component = inputBlock.getPanelVariables().getComponent();
            UIMaker.setLeftPadding(component, UIMaker.PADDING * (paddingScale+1));
            panel.add(component);
            listInputBlock.add(inputBlock);
        }
    }

    private void addDirectory(TemplateElement element) {
        // TODO: 17.06.2016 add separator
        InputBlock inputBlock = new InputBlock(element, paddingScale, null);
        panel.add(UIMaker.getDirectoryPanel(inputBlock, paddingScale));
        listInputBlock.add(inputBlock);

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

    public void buildPanel() {
        //add class field
//        block.getTfName()
        //add vars pane
    }

    public void initGlobalProperties() {
        mapGlobalProperties = new HashMap<>();
        for( InputBlock block : getListInputBlock()){
            if(block.isGlobalVariable()){
                mapGlobalProperties.put(block.getGlobalKey(), block.getTfName().getText());
            }
        }
    }

    public HashMap<String, String> getMapGlobalProperties() {
        return mapGlobalProperties;
    }
}
