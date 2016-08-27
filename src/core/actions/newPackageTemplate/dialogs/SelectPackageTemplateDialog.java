package core.actions.newPackageTemplate.dialogs;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.GridBag;
import global.listeners.ReleaseListener;
import global.models.TemplateListModel;
import global.models.PackageTemplate;
import core.state.export.dialogs.ImpexDialog;
import org.jetbrains.annotations.NotNull;
import core.state.SaveUtil;
import core.state.models.StateModel;
import global.utils.GridBagFactory;
import global.utils.Localizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends BaseDialog {

    private JBList jbList;
    private ArrayList<PackageTemplate> templateList;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onCancel();

    public SelectPackageTemplateDialog(Project project) {
        super(project);
        setTitle(Localizer.get("SelectFileTemplate"));
    }


    @Override
    protected ValidationInfo doValidate() {
        if (jbList.isSelectionEmpty()) {
            return new ValidationInfo(Localizer.get("SelectItem"), jbList);
        }

//        ValidationInfo validationInfo = TemplateValidator.isTemplateValid((PackageTemplate) jbList.getSelectedValue());
//        if (validationInfo != null) {
//            return new ValidationInfo(validationInfo.message, jbList);
//        }

        return null;
    }

    @Override
    public void preShow() {

    }

    @Override
    public void onOKAction() {
        onSuccess((PackageTemplate) jbList.getSelectedValue());
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }

    @Override
    protected JComponent createCenterPanel() {
        JSplitPane root = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        root.setMinimumSize(new Dimension(400, 300));
        root.setEnabled(false);


        StateModel stateModel = SaveUtil.getInstance().getStateModel();
        templateList = stateModel.getListPackageTemplate();
        jbList = new JBList();
        initJBList();

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
        ImpexDialog dialog = new ImpexDialog(project, "Export Templates") {
            @Override
            public void onSuccess() {
                System.out.println("onSuccess");
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }
        };
        dialog.show();

//            templateList.remove(jbList.getSelectedValue());
//            SaveUtil.getInstance().save();
//            initJBList();

    }

    private void onEditAction() {
        if (!jbList.isSelectionEmpty()) {
            ConfigurePackageTemplatesDialog dialog = new ConfigurePackageTemplatesDialog(project, ((PackageTemplate) jbList.getSelectedValue())) {
                @Override
                public void onSuccess(PackageTemplate packageTemplate) {
                    SaveUtil.getInstance().save();
                }

                @Override
                public void onFail() {
                }
            };
            dialog.show();
        }
    }

    private void onDeleteAction(JButton jbDelete) {
        if (!jbList.isSelectionEmpty()) {
            int confirmDialog = JOptionPane.showConfirmDialog(jbDelete, Localizer.get("DeleteTemplate"));
            if (confirmDialog == JOptionPane.OK_OPTION) {
                templateList.remove(jbList.getSelectedValue());
                SaveUtil.getInstance().save();
                initJBList();
            }
        }
    }

    private void onAddAction() {
        ConfigurePackageTemplatesDialog dialog = new ConfigurePackageTemplatesDialog(project) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                templateList.add(packageTemplate);
                SaveUtil.getInstance().save();
                initJBList();
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
    }

    @NotNull
    private JButton getIconButton(Icon icon) {
        JButton jButton = new JButton(icon);
        jButton.setBorder(BorderFactory.createEmptyBorder());
        return jButton;
    }

    private void initJBList() {
        jbList.removeAll();
        jbList.setModel(new TemplateListModel<>(templateList));

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
