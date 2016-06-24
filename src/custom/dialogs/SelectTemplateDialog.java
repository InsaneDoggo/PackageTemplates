package custom.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.ListCellRendererWrapper;
import custom.TemplateCellRender;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.ide.fileTemplates.FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY;

/**
 * Created by CeH9 on 24.06.2016.
 */
public class SelectTemplateDialog extends DialogWrapper {

    public SelectTemplateDialog(Project project) {
        super(project);
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setAlignmentY(Component.TOP_ALIGNMENT);
        //todo filters

        ComboBox comboBox = getSelector();

        root.add(comboBox);
        return root;
    }


    @NotNull
    private ComboBox getSelector() {
        FileTemplateManager ftManager = FileTemplateManager.getDefaultInstance();
        FileTemplate[] fileTemplates = ftManager.getTemplates(DEFAULT_TEMPLATES_CATEGORY);

        ComboBox comboBox = new ComboBox(fileTemplates);
        comboBox.setRenderer(new ListCellRendererWrapper<FileTemplate>() {
            @Override
            public void customize(JList list, FileTemplate fileTemplate, int index, boolean selected, boolean hasFocus) {
                if (fileTemplate != null) {
                    setIcon(FileTemplateUtil.getIcon(fileTemplate));
                    setText(fileTemplate.getName());
                }
            }
        });


//        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getPreferredSize().height));
//        ComboboxSpeedSearch speedSearch = new ComboboxSpeedSearch(comboBox);

        return comboBox;
    }
}
