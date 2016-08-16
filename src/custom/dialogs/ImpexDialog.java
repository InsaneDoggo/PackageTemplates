package custom.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollapsiblePanel;
import com.intellij.ui.TabbedPaneImpl;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import state.SaveUtil;
import state.export.models.ExpFileTemplateWrapper;
import state.export.models.ExpPackageTemplateWrapper;
import state.models.StateModel;
import utils.FileWriter;
import utils.GridBagFactory;
import utils.Localizer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by CeH9 on 04.07.2016.
 */

public abstract class ImpexDialog extends DialogWrapper {

    public abstract void onSuccess();

    public abstract void onCancel();

    private Project project;
    private TextFieldWithBrowseButton tfbButton;
    private ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper;

    public ImpexDialog(@Nullable Project project, String title) {
        super(project);
        this.project = project;
        init();
        setTitle(title);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case ImpexDialog.OK_EXIT_CODE:
                String content = SaveUtil.getInstance().getTemplatesForExport();
                FileWriter.exportFile(tfbButton.getText(), "Templates.json", content);
                onSuccess();
                break;
            case ImpexDialog.CANCEL_EXIT_CODE:
                onCancel();
                break;
        }
    }

    @Override
    protected ValidationInfo doValidate() {
        if (tfbButton.getText().isEmpty()) {
            return new ValidationInfo(Localizer.get("FillEmptyFields"));
        }

        return null;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        TabbedPaneImpl tabContainer = new TabbedPaneImpl(SwingConstants.TOP);
        tabContainer.setMinimumSize(new Dimension(640, 480));
        tabContainer.setKeyboardNavigation(TabbedPaneImpl.DEFAULT_PREV_NEXT_SHORTCUTS);

        tabContainer.addTab(Localizer.get("Export"), AllIcons.General.ExportSettings, getExportTab());
        tabContainer.addTab(Localizer.get("Import"), AllIcons.General.ImportSettings, new JPanel());
        return tabContainer;
    }

    @NotNull
    private JPanel getExportTab() {
        JPanel panelExport = new JPanel();
        panelExport.setLayout(new GridBagLayout());
        GridBag bag = GridBagFactory.getGridBagForImpexDialog();

        tfbButton = new TextFieldWithBrowseButton();
        tfbButton.addBrowseFolderListener(Localizer.get("ExportTemplatesTo"), "", project,
                new FileChooserDescriptor(false, true, false, false, false, false));


        StateModel stateModel = SaveUtil.getInstance().getStateModel();
        listExpPackageTemplateWrapper = new ArrayList<>();
        for (PackageTemplate pt : stateModel.getListPackageTemplate()) {
            ExpPackageTemplateWrapper eptWrapper = new ExpPackageTemplateWrapper(true, pt);
            listExpPackageTemplateWrapper.add(eptWrapper);

            JPanel content = new JPanel(new GridBagLayout());
            GridBag gBag = GridBagFactory.getGridBagForImpexDialog();
            for (ExpFileTemplateWrapper eftWrapper : eptWrapper.getListExpFileTemplateWrapper()) {
                content.add(eftWrapper.cbInclude, gBag.nextLine().next());
                content.add(eftWrapper.jlName, gBag.next());
            }

            CollapsiblePanel collapsiblePanel = new CollapsiblePanel(
                    content,
                    true,
                    true,
                    AllIcons.Nodes.CollapseNode,
                    AllIcons.Nodes.ExpandNode,
                    "   " + pt.getName()
            );

            panelExport.add(eptWrapper.cbInclude, bag.nextLine().next().anchor(GridBagConstraints.NORTH));
//            panelExport.add(eptWrapper.jlName, bag.next());
            panelExport.add(collapsiblePanel, bag.next().insets(0, 24, 0, 0));
        }

        //panelExport.add(tfbButton, bag.nextLine().next());
        return panelExport;
    }

}










