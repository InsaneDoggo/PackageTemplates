package core.state.impex.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.TabbedPaneImpl;
import com.intellij.ui.components.JBScrollPane;
import core.state.impex.models.ExpPackageTemplateWrapper;
import global.utils.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.intellij.icons.AllIcons.General.ExportSettings;
import static com.intellij.icons.AllIcons.General.ImportSettings;

/**
 * Created by CeH9 on 04.07.2016.
 */

public abstract class ImpexDialog extends DialogWrapper implements ImpexView {

    public abstract void onSuccess();

    public abstract void onCancel();

    private Project project;
    private ImpexPresenter presenter;

    public ImpexDialog(@Nullable Project project, String title) {
        super(project);
        this.project = project;
        presenter = new ImpexPresenter(this);
        init();
        setTitle(title);

    }

    @Override
    protected ValidationInfo doValidate() {
        return presenter.doValidate(exportTab.getBtnPath().getText());
    }

    @Override
    protected void doOKAction() {
        presenter.exportTemplates(exportTab.getBtnPath().getText());
        super.doOKAction();
        onSuccess();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        onCancel();
    }

    private TabbedPaneImpl tabContainer;

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        tabContainer = new TabbedPaneImpl(JTabbedPane.TOP);
        tabContainer.setKeyboardNavigation(TabbedPaneImpl.DEFAULT_PREV_NEXT_SHORTCUTS);
        tabContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        tabContainer.addChangeListener(e -> {
            String title = tabContainer.getTitleAt(tabContainer.getSelectedIndex());

            if(title.equals(Localizer.get("Import"))){
                presenter.setCurrentTab(ImpexPresenter.TabType.IMPORT);
                return;
            }
            if(title.equals(Localizer.get("Export"))){
                presenter.setCurrentTab(ImpexPresenter.TabType.EXPORT);
                return;
            }
        });

        presenter.onCreateCenterPanel();

        return new JBScrollPane(tabContainer);
    }

    private ImportTab importTab;

    @Override
    public void addImportTab() {
        importTab = new ImportTab(project);
        tabContainer.addTab(Localizer.get("Import"), ImportSettings, wrapInDummyPanel(importTab.getPanel()));
    }

    private ExportTab exportTab;

    @Override
    public void addExportTab(ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper) {
        exportTab = new ExportTab(project, listExpPackageTemplateWrapper);
        tabContainer.addTab(Localizer.get("Export"), ExportSettings, wrapInDummyPanel(exportTab.getPanel()));
    }

    private JPanel wrapInDummyPanel(JPanel content) {
        JPanel panel = new JPanel(new MigLayout());
        panel.add(content, new CC().grow().alignY("top"));
        return panel;
    }

}










