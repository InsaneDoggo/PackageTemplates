package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.GridBag;
import global.listeners.ReleaseListener;
import global.models.PackageTemplate;
import global.models.TemplateListModel;
import global.utils.GridBagFactory;
import global.utils.Localizer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends BaseDialog implements SelectPackageTemplateView {

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onCancel();

    private JBList jbList;
    private SelectPackageTemplatePresenter presenter;

    public SelectPackageTemplateDialog(Project project) {
        super(project);
    }

    @Override
    protected ValidationInfo doValidate() {
        if (jbList.isSelectionEmpty()) {
            return new ValidationInfo(Localizer.get("SelectItem"), jbList);
        }

        return presenter.doValidate((PackageTemplate) jbList.getSelectedValue(), jbList);
    }

    @Override
    public void onOKAction() {
        presenter.onSuccess((PackageTemplate) jbList.getSelectedValue());
    }

    @Override
    public void onCancelAction() {
        presenter.onCancel();
    }

    @Override
    protected JComponent createCenterPanel() {
        presenter = new SelectPackageTemplatePresenterImpl(this, project);

        JSplitPane root = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        root.setMinimumSize(new Dimension(400, 300));
        root.setEnabled(false);

        jbList = new JBList();
        presenter.loadTemplates();

        root.add(getControls());
        root.add(jbList);

        return root;
    }

    private Component getControls() {
        JPanel container = new JPanel(new GridBagLayout());
        GridBag bag = GridBagFactory.getBagForSelectDialog();

        JButton jbAdd = getIconButton(AllIcons.General.Add);
        JButton jbDelete = getIconButton(AllIcons.General.Remove);
        JButton jbEdit = getIconButton(AllIcons.Modules.Edit);
        JButton jbExport = getIconButton(AllIcons.Graph.Export);

        jbAdd.addMouseListener(new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                onAddAction();
            }
        });
        jbDelete.addMouseListener(new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    onDeleteAction(jbDelete);
                }
            }
        });
        jbEdit.addMouseListener(new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    onEditAction();
                }
            }
        });
        jbExport.addMouseListener(new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    onExportAction();
                }
            }
        });

        container.add(jbAdd, bag.nextLine().next());
        container.add(jbDelete, bag.next());
        container.add(jbEdit, bag.next());
        container.add(jbExport, bag.next());

        return container;
    }

    private void onExportAction() {
        presenter.onExportAction();
    }

    private void onEditAction() {
        if (!jbList.isSelectionEmpty()) {
            presenter.onEditAction((PackageTemplate) jbList.getSelectedValue());
        }
    }

    private void onDeleteAction(JButton jbDelete) {
        if (!jbList.isSelectionEmpty()) {
            presenter.onDeleteAction(jbDelete, (PackageTemplate) jbList.getSelectedValue());
        }
    }

    private void onAddAction() {
        presenter.onAddAction();
    }

    @NotNull
    private JButton getIconButton(Icon icon) {
        JButton jButton = new JButton(icon);
        jButton.setBorder(BorderFactory.createEmptyBorder());
        return jButton;
    }

    @Override
    public void setTemplatesList(TemplateListModel<PackageTemplate> list) {
        jbList.removeAll();
        jbList.setModel(list);

        jbList.setCellRenderer(new ListCellRendererWrapper<PackageTemplate>() {
            @Override
            public void customize(JList list, PackageTemplate template, int index, boolean selected, boolean hasFocus) {
                if (template != null) {
                    setText(template.getName());
                }
            }
        });
    }

}
