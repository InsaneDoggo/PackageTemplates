package custom.dialogs;

import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.TabbedPaneImpl;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.util.ui.GridBag;
import custom.view.SpoilerPane;
import javafx.scene.layout.VBox;
import models.PackageTemplate;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
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

import static com.intellij.icons.AllIcons.General.ExportSettings;
import static com.intellij.icons.AllIcons.General.ImportSettings;
import static com.intellij.icons.AllIcons.Nodes.CollapseNode;
import static com.intellij.icons.AllIcons.Nodes.ExpandNode;

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
        TabbedPaneImpl tabContainer = new TabbedPaneImpl(JTabbedPane.TOP);
//        tabContainer.setMinimumSize(new Dimension(640, 480));
        tabContainer.setKeyboardNavigation(TabbedPaneImpl.DEFAULT_PREV_NEXT_SHORTCUTS);
        tabContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        tabContainer.addTab(Localizer.get("Export"), ExportSettings, getExportTab());
        tabContainer.addTab(Localizer.get("Import"), ImportSettings, new JPanel());
        return tabContainer;
    }

    @NotNull
    private JPanel getExportTab() {
        JPanel panelExport = new JPanel(new MigLayout());

        tfbButton = new TextFieldWithBrowseButton();
        tfbButton.addBrowseFolderListener(Localizer.get("ExportTemplatesTo"), "", project,
                new FileChooserDescriptor(false, true, false, false, false, false));

        StateModel stateModel = SaveUtil.getInstance().getStateModel();
        listExpPackageTemplateWrapper = new ArrayList<>();
        for (PackageTemplate pt : stateModel.getListPackageTemplate()) {
            ExpPackageTemplateWrapper eptWrapper = new ExpPackageTemplateWrapper(true, pt);
            listExpPackageTemplateWrapper.add(eptWrapper);

            // File templates
            JPanel content = new JPanel(new MigLayout());
            for (ExpFileTemplateWrapper eftWrapper : eptWrapper.getListExpFileTemplateWrapper()) {
                content.add(eftWrapper.cbInclude, new CC());
                content.add(eftWrapper.jlName, new CC().wrap().gapX("8", "0"));
            }

            // Package Template
            panelExport.add(eptWrapper.cbInclude, new CC().alignY("top"));
            SpoilerPane spoilerPane = new SpoilerPane(content, ExpandNode, CollapseNode, pt.getName());
            panelExport.add(spoilerPane, new CC().wrap().alignY("top").gapX("8", "0"));
        }

        //panelExport.add(tfbButton, bag.nextLine().next());
        return wrapInDummyPanel(panelExport);
    }

    private JPanel wrapInDummyPanel(JPanel content) {
        JPanel panel = new JPanel(new MigLayout());
        panel.add(content, new CC().grow().alignY("top"));
        return panel;
    }

}










