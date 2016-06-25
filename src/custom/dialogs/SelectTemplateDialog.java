package custom.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.ListCellRendererWrapper;
import models.TemplateForSearch;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.intellij.ide.fileTemplates.FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectTemplateDialog extends DialogWrapper {

    private ComboBox comboBox;

    public abstract void onSuccess(FileTemplate fileTemplate);
    public abstract void onCancel();

    public SelectTemplateDialog(Project project) {
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
        //todo filters

        comboBox = getSelector();

        root.add(comboBox);
        return root;
    }


    @NotNull
    private ComboBox getSelector() {
        FileTemplate[] fileTemplates = FileTemplateManager.getDefaultInstance().getTemplates(DEFAULT_TEMPLATES_CATEGORY);

        ArrayList<TemplateForSearch> listTemplateForSearch = new ArrayList<>(fileTemplates.length);
        for( FileTemplate template : fileTemplates ){
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
}
