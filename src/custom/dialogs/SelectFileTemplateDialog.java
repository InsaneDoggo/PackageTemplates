package custom.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBInsets;
import models.TemplateForSearch;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectFileTemplateDialog extends DialogWrapper {

    private ComboBox comboBox;

    public abstract void onSuccess(FileTemplate fileTemplate);

    public abstract void onCancel();

    public SelectFileTemplateDialog(Project project) {
        super(project);
        init();
        setTitle("Select File Template");
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case NewPackageDialog.OK_EXIT_CODE:
                onSuccess(((TemplateForSearch) comboBox.getSelectedItem()).getTemplate());
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onCancel();
                break;
        }
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel options = new JPanel(new GridBagLayout());
        GridBag bag = new GridBag()
                .setDefaultInsets(new JBInsets(0,4,0,4))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        cbAddInternal = new JBCheckBox("Internal");
        cbAddJ2EE = new JBCheckBox("J2EE");

        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                root.remove(comboBox);
                comboBox = getSelector();
                root.add(comboBox);
                root.revalidate();
            }
        };

        cbAddInternal.addItemListener(itemListener);
        cbAddJ2EE.addItemListener(itemListener);

        options.add(cbAddInternal, bag.nextLine().next());
        options.add(cbAddJ2EE, bag.next());

        comboBox = getSelector();

        root.add(options);
        root.add(Box.createRigidArea(new Dimension(0,10)));
        root.add(comboBox);

        root.setMinimumSize(new Dimension(300, root.getMinimumSize().height));
        return root;
    }


    @NotNull
    private ComboBox getSelector() {
        FileTemplate[] fileTemplates = getFileTemplates();

        ArrayList<TemplateForSearch> listTemplateForSearch = new ArrayList<>(fileTemplates.length);
        for (FileTemplate template : fileTemplates) {
            listTemplateForSearch.add(new TemplateForSearch(template));
        }

        ComboBox comboBox = new ComboBox(listTemplateForSearch.toArray());
        comboBox.setRenderer(new ListCellRendererWrapper<TemplateForSearch>() {
            @Override
            public void customize(JList list, TemplateForSearch template, int index, boolean selected, boolean hasFocus) {
                if (template != null) {
                    setIcon(FileTemplateUtil.getIcon(template.getTemplate()));
                    setText(template.getTemplate().getName());
                }
            }
        });

        new ComboboxSpeedSearch(comboBox);
        return comboBox;
    }

    JBCheckBox cbAddInternal;
    JBCheckBox cbAddJ2EE;

    private FileTemplate[] getFileTemplates() {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();
        FileTemplate[] result = ftm.getAllTemplates();

        if (cbAddInternal.isSelected())
            result = ArrayUtil.mergeArrays(result, ftm.getInternalTemplates());
        if (cbAddJ2EE.isSelected())
            result = ArrayUtil.mergeArrays(result, ftm.getTemplates(FileTemplateManager.J2EE_TEMPLATES_CATEGORY));

        return result;
    }
}
