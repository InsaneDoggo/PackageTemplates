package core.state.export.dialogs;

import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.TabbedPaneImpl;
import com.intellij.ui.components.JBScrollPane;
import global.views.SpoilerPane;
import global.models.PackageTemplate;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import core.state.export.models.ExpFileTemplateWrapper;
import core.state.export.models.ExpPackageTemplateWrapper;
import global.utils.Localizer;

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

public abstract class ImpexDialog extends DialogWrapper implements ImpexView {

    public abstract void onSuccess();

    public abstract void onCancel();

    private Project project;
    private TextFieldWithBrowseButton tfbButton;

    private ImpexPresenter presenter;

    public ImpexDialog(@Nullable Project project, String title) {
        super(project);
        this.project = project;
        presenter = new ImpexPresenter(this);
        init();
        setTitle(title);

    }

    @Override
    public void show() {
        super.show();
        presenter.onExit(getExitCode(), tfbButton.getText());
    }

    @Override
    protected ValidationInfo doValidate() {
        return presenter.doValidate(tfbButton.getText());
    }

    private TabbedPaneImpl tabContainer;

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        tabContainer = new TabbedPaneImpl(JTabbedPane.TOP);
        tabContainer.setMinimumSize(new Dimension(640, 480));
        tabContainer.setKeyboardNavigation(TabbedPaneImpl.DEFAULT_PREV_NEXT_SHORTCUTS);
        tabContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        presenter.onCreateCenterPanel();

        return new JBScrollPane(tabContainer);
    }

    @Override
    public void addImportTab() {
        tabContainer.addTab(Localizer.get("Import"), ImportSettings, new JPanel());
    }

    @Override
    public void addExportTab(ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper) {
        JPanel panelExport = new JPanel(new MigLayout());

        tfbButton = new TextFieldWithBrowseButton();
        tfbButton.addBrowseFolderListener(Localizer.get("ExportTemplatesTo"), "", project,
                new FileChooserDescriptor(false, true, false, false, false, false));


        for (ExpPackageTemplateWrapper eptWrapper : listExpPackageTemplateWrapper) {
            // File templates
            JPanel content = new JPanel(new MigLayout());
            for (ExpFileTemplateWrapper eftWrapper : eptWrapper.getListExpFileTemplateWrapper()) {
                content.add(eftWrapper.cbInclude, new CC());
                content.add(eftWrapper.jlName, new CC().wrap().gapX("8", "0"));
            }

            // Package Template
            panelExport.add(eptWrapper.cbInclude, new CC().alignY("top"));
            SpoilerPane spoilerPane = new SpoilerPane(content, ExpandNode, CollapseNode, eptWrapper.getName());
            panelExport.add(spoilerPane, new CC().wrap().alignY("top").gapX("8", "0"));
        }

        panelExport.add(tfbButton, new CC().spanX().wrap().gapY("12", "12"));
        tabContainer.addTab(Localizer.get("Export"), ExportSettings, wrapInDummyPanel(panelExport));
    }

    private JPanel wrapInDummyPanel(JPanel content) {
        JPanel panel = new JPanel(new MigLayout());
        panel.add(content, new CC().grow().alignY("top"));
        return panel;
    }

}










