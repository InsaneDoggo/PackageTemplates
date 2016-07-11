package utils;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import models.InputBlock;
import models.PackageTemplate;
import models.TemplateElement;
import org.apache.velocity.runtime.parser.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Arsen on 15.06.2016.
 */
public class InputManager {

    private int paddingScale = 0;

    private JPanel panel;
    private AnActionEvent event;
    private PackageTemplate packageTemplate;
    private Project project;
    private ArrayList<InputBlock> listInputBlock;
    private HashMap<String, String> mapGlobalProperties;

    public static final ArrayList<String> listAttributesToRemove = new ArrayList<String>() {{
        add(FileTemplate.ATTRIBUTE_NAME);
        add(FileTemplate.ATTRIBUTE_PACKAGE_NAME);
        add(PackageTemplate.ATTRIBUTE_BASE_NAME);
    }};

    public InputManager(AnActionEvent event, PackageTemplate packageTemplate) {
        this.project = event.getProject();
        this.packageTemplate = packageTemplate;
        this.event = event;

        listInputBlock = new ArrayList<>();
        initPanel();
    }

    public ArrayList<InputBlock> getListInputBlock() {
        return listInputBlock;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setMinimumSize(new Dimension(UIMaker.DIALOG_MIN_WIDTH, panel.getPreferredSize().height));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addHeader(panel);

        paddingScale++;
    }

    @NotNull
    private void addHeader(JPanel panel) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());

        GridBag bag = GridBagFactory.getDefaultGridBag();

        // Add globals blocks
        for (Map.Entry<String, String> entry : packageTemplate.getMapGlobalVars().entrySet()) {
            InputBlock inputBlock = new InputBlock(packageTemplate.getTemplateElement(), null, project);
            inputBlock.setGlobalVariable(true);
            inputBlock.setGlobalKey(entry.getKey());
            inputBlock.setGlobalValue(entry.getValue());
            listInputBlock.add(inputBlock);

            JLabel jLabel = new JLabel(AllIcons.Nodes.Variable);
            jLabel.setText(inputBlock.getGlobalKey());
            jLabel.setHorizontalAlignment(SwingConstants.LEFT);
            jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            UIMaker.setHorizontalPadding(jLabel, UIMaker.PADDING_LABEL);

            inputBlock.getTfName().setText(entry.getValue());
            inputBlock.getTfName().setAlignmentX(Component.LEFT_ALIGNMENT);

            container.add(jLabel, bag.nextLine().next());
            container.add(inputBlock.getTfName(), bag.next());
        }

        panel.add(container);
    }

    public JPanel getPanel() {
        return panel;
    }

    public AnActionEvent getEvent() {
        return event;
    }

    public Project getProject() {
        return project;
    }

    public PackageTemplate getPackageTemplate() {
        return packageTemplate;
    }

    public void addElement(TemplateElement element) {
        if (element.isDirectory()) {
            addDirectory(element);
        } else {
            addClass(element);
        }
    }

    private void addClass(TemplateElement element) {
        FileTemplate fileTemplate = getTemplate(element.getTemplateName());
        if (fileTemplate != null) {
            InputBlock inputBlock = new InputBlock(element, getUnsetAttrs(fileTemplate), project);
            panel.add(UIMaker.getClassPanel(inputBlock, paddingScale, UIMaker.getIconByFileExtension(element.getExtension())));

            if (inputBlock.getPanelVariables() != null) {
                // add variables
                JComponent component = inputBlock.getPanelVariables().getComponent();
                UIMaker.setLeftPadding(component, UIMaker.PADDING * (paddingScale + 1));
                panel.add(component);
            }

            listInputBlock.add(inputBlock);
        }
    }

    @Nullable
    public static FileTemplate getTemplate(String name) {
        FileTemplate fileTemplate = FileTemplateManager.getDefaultInstance().getTemplate(name);
        if (fileTemplate != null) {
            return fileTemplate;
        } else {
            return FileTemplateManager.getDefaultInstance().getDefaultTemplate(name);
        }
    }

    private void addDirectory(TemplateElement element) {
        InputBlock inputBlock = new InputBlock(element, null, project);
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
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (listAttributesToRemove.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void onPackageEnds() {
        paddingScale--;
    }

    public void collectGlobalVars() {
        mapGlobalProperties = new HashMap<>();
        for (InputBlock block : getListInputBlock()) {
            if (block.isGlobalVariable()) {
                block.setGlobalValue(block.getTfName().getText());
                mapGlobalProperties.put(block.getGlobalKey(), block.getGlobalValue());
            }
        }
    }

    public HashMap<String, String> getMapGlobalProperties() {
        return mapGlobalProperties;
    }
}
