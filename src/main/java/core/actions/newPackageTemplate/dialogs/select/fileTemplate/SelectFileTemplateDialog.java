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
import global.utils.templates.FileTemplateHelper;
import global.wrappers.PackageTemplateWrapper;
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
    private Project project;
    private PackageTemplateWrapper ptWrapper;

    public SelectFileTemplateDialog(Project project, PackageTemplateWrapper ptWrapper) {
        super(project);
        this.project = project;
        this.ptWrapper = ptWrapper;
        init();
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    public abstract void onSuccess(FileTemplate fileTemplate);

    public abstract void onCancel();

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

    @Override
    protected JComponent createCenterPanel() {
        presenter = new SelectFileTemplatePresenterImpl(this);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel options = getOptionsPanel(root);

        comboBox = getSelector();

        root.add(options);
        root.add(Box.createRigidArea(new Dimension(0, 10)));
        root.add(comboBox);

        root.setMinimumSize(new Dimension(300, root.getMinimumSize().height));
        return root;
    }


    //=================================================================
    //  Options / Filter
    //=================================================================
    private JBCheckBox cbAddInternal;
    private JBCheckBox cbAddJ2EE;

    @NotNull
    private JPanel getOptionsPanel(final JPanel root) {
        JPanel options = new JPanel(new GridBagLayout());
        GridBag bag = new GridBag()
                .setDefaultInsets(new JBInsets(0, 4, 0, 4))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        cbAddInternal = new JBCheckBox("Internal");
        cbAddJ2EE = new JBCheckBox("J2EE");

        ItemListener itemListener = e -> {
            root.remove(comboBox);
            comboBox = getSelector();
            root.add(comboBox);
            root.revalidate();
        };

        cbAddInternal.addItemListener(itemListener);
        cbAddJ2EE.addItemListener(itemListener);

        options.add(cbAddInternal, bag.nextLine().next());
        options.add(cbAddJ2EE, bag.next());
        return options;
    }


    //=================================================================
    //  Other
    //=================================================================
    @NotNull
    private ComboBox getSelector() {
        ArrayList<TemplateForSearch> listTemplateForSearch = FileTemplateHelper.getTemplates(
                project,
                cbAddInternal.isSelected(),
                cbAddJ2EE.isSelected(),
                ptWrapper.getPackageTemplate().getFileTemplateSource()
        );

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
