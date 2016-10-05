package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.GridBag;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.controls.Control;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.controls.ControlsAdapter;
import global.listeners.ReleaseListener;
import global.models.PackageTemplate;
import global.models.TemplateListModel;
import global.utils.GridBagFactory;
import global.utils.Localizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

    @NotNull
    @Override
    protected Action getOKAction() {
        Action action = super.getOKAction();
        action.putValue(Action.NAME, Localizer.get("action.Select"));
        return action;
    }

    @NotNull
    @Override
    protected Action getCancelAction() {
        Action action = super.getCancelAction();
        action.putValue(Action.NAME, Localizer.get("action.Cancel"));
        return action;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return jbList;
    }

    @Override
    protected JComponent createCenterPanel() {
        presenter = new SelectPackageTemplatePresenterImpl(this, project);

        JSplitPane root = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        root.setMinimumSize(new Dimension(400, 300));
        root.setEnabled(false);

        jbList = new JBList();
        jbList.setCellRenderer(new ListCellRendererWrapper<PackageTemplate>() {
            @Override
            public void customize(JList list, PackageTemplate template, int index, boolean selected, boolean hasFocus) {
                if (template != null) {
                    setText(template.getName());
                }
            }
        });
        presenter.loadTemplates();
        jbList.setSelectedIndex(0);

        root.add(getControls());
        root.add(jbList);

        return root;
    }

    private Component getControls() {
        JPanel container = new JPanel(new GridBagLayout());
        GridBag bag = GridBagFactory.getBagForSelectDialog();

        ControlsAdapter controlsAdapter = new ControlsAdapter(container, createActionButtons(), bag);
        controlsAdapter.buildView();

        return container;
    }

    private ArrayList<Control> createActionButtons() {
        ArrayList<Control> listControls = new ArrayList<>();

        listControls.add(new Control(AllIcons.General.Add, new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                presenter.onAddAction();
            }
        }));
        listControls.add(new Control(AllIcons.General.Remove, new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    if (!jbList.isSelectionEmpty()) {
                        presenter.onDeleteAction((PackageTemplate) jbList.getSelectedValue());
                    }
                }
            }
        }));
        listControls.add(new Control(AllIcons.Modules.Edit, new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    if (!jbList.isSelectionEmpty()) {
                        presenter.onEditAction((PackageTemplate) jbList.getSelectedValue());
                    }
                }
            }
        }));
//        listControls.add(new Control(AllIcons.Graph.Export, new ReleaseListener() {
//            @Override
//            public void mouseReleased(MouseEvent event) {
//                if (SwingUtilities.isLeftMouseButton(event)) {
//                    presenter.onExportAction();
//                }
//            }
//        }));
        listControls.add(new Control(AllIcons.General.Settings, new ReleaseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    presenter.onSettingsAction();
                }
            }
        }));
        return listControls;
    }

    @Override
    public void setTemplatesList(TemplateListModel<PackageTemplate> list) {
        jbList.removeAll();
        jbList.setModel(list);
    }

}
