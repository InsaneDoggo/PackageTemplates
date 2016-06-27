package custom.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import io.SaveUtil;
import models.PackageTemplate;
import models.TemplateForSearch;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.intellij.ide.fileTemplates.FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends DialogWrapper {

    private JBList jbList;

    public abstract void onSuccess(PackageTemplate packageTemplate);
    public abstract void onCancel();

    public SelectPackageTemplateDialog(Project project) {
        super(project);
        init();
        setTitle("Select File Template");
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case NewPackageDialog.OK_EXIT_CODE:
                if( jbList.isSelectionEmpty() ){
                    // TODO: 27.06.2016 selection empty
                    return;
                }
                onSuccess((PackageTemplate) jbList.getSelectedValue());
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

        jbList = getSelector();

        root.add(jbList);
        return root;
    }


    @NotNull
    private JBList getSelector() {
        JBList jList = new JBList(SaveUtil.getInstance().getTemplateList());

        jList.setCellRenderer(new ListCellRendererWrapper<PackageTemplate>() {
            @Override
            public void customize(JList list, PackageTemplate template, int index, boolean selected, boolean hasFocus) {
                if (template != null) {
                    setText(template.getName());
                }
            }
        });

        return jList;
    }
}
