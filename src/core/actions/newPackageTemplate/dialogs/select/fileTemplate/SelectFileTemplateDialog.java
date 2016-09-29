package core.actions.newPackageTemplate.dialogs.select.fileTemplate;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBInsets;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import global.models.TemplateForSearch;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectFileTemplateDialog extends DialogWrapper implements SelectFileTemplateView {

    private ComboBox comboBox;
    private SelectFileTemplatePresenter presenter;

    public abstract void onSuccess(FileTemplate fileTemplate);
    public abstract void onCancel();

    public SelectFileTemplateDialog(Project project) {
        super(project);
        init();
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case ImplementDialog.OK_EXIT_CODE:
                presenter.onSuccess(((TemplateForSearch) comboBox.getSelectedItem()).getTemplate());
                break;
            case ImplementDialog.CANCEL_EXIT_CODE:
                presenter.onCancel();
                break;
        }
    }

    private JBCheckBox cbAddInternal;
    private JBCheckBox cbAddJ2EE;

    @Override
    protected JComponent createCenterPanel() {
        presenter = new SelectFileTemplatePresenterImpl(this);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel options = new JPanel(new GridBagLayout());
        GridBag bag = new GridBag()
                .setDefaultInsets(new JBInsets(0, 4, 0, 4))
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
        root.add(Box.createRigidArea(new Dimension(0, 10)));
        root.add(comboBox);

        root.setMinimumSize(new Dimension(300, root.getMinimumSize().height));
        return root;
    }


    @NotNull
    private ComboBox getSelector() {
        ArrayList<TemplateForSearch> listTemplateForSearch = presenter.getListTemplateForSearch(cbAddInternal.isSelected(), cbAddJ2EE.isSelected());

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
