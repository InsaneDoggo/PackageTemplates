package core.state.impex.dialogs;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import core.state.util.MigrationHelper;
import core.state.impex.models.ExpFileTemplateWrapper;
import core.state.impex.models.ExpPackageTemplate;
import core.state.impex.models.ExpPackageTemplateWrapper;
import core.state.impex.models.ExportBundle;
import global.listeners.ClickListener;
import global.utils.FileReaderUtil;
import global.utils.GsonFactory;
import global.utils.i18n.Localizer;
import global.views.SpoilerPane;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.intellij.icons.AllIcons.Nodes.CollapseNode;
import static com.intellij.icons.AllIcons.Nodes.ExpandNode;

/**
 * Created by Arsen on 04.09.2016.
 */
public class ImportTab {

    private Project project;
    private ExportBundle bundle;

    public ImportTab(Project project) {
        this.project = project;
        init();
    }

    private JPanel rootPanel;
    private TextFieldWithBrowseButton btnPath;

    private void init() {
        rootPanel = new JPanel(new MigLayout());
        rootPanel.setMinimumSize(new Dimension(480, 480));

        btnPath = new TextFieldWithBrowseButton();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        descriptor.withFileFilter(file -> {
            if (file.getExtension() == null) return false;
            return (file.getExtension().toLowerCase().contains("json"));
        });
        btnPath.addBrowseFolderListener(Localizer.get("ExportTemplatesTo"), "", project, descriptor);

        JButton btnOpen = new JButton(Localizer.get("Open"));
        btnOpen.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent eventOuter) {
                if (SwingUtilities.isLeftMouseButton(eventOuter)) {
                    String content = FileReaderUtil.readFile(btnPath.getText());
                    bundle = GsonFactory.getInstance().fromJson(content, ExportBundle.class);
                    if( bundle != null){
                        MigrationHelper.upgrade(bundle);
                        showTemplates();
                    }
                    System.out.println("LMB");
                }
            }
        });

        JPanel pathPanel = new JPanel(new MigLayout(new LC().fillX()));
        pathPanel.add(btnPath, new CC().pushX().growX());
        pathPanel.add(btnOpen, new CC().wrap());

        rootPanel.add(pathPanel, new CC().spanX().wrap().growX().gapY("12", "12"));
    }

    private ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper;

    private void showTemplates() {
        listExpPackageTemplateWrapper = new ArrayList<>();

        //todo check selected FileTemplates
        for (ExpPackageTemplate ept : bundle.getListPackageTemplate()) {
            listExpPackageTemplateWrapper.add(new ExpPackageTemplateWrapper(true, ept));
        }

        for (ExpPackageTemplateWrapper eptWrapper : listExpPackageTemplateWrapper) {
            // File templates
            JPanel content = new JPanel(new MigLayout(new LC().fillX()));
            for (ExpFileTemplateWrapper eftWrapper : eptWrapper.getListExpFileTemplateWrapper()) {
                content.add(eftWrapper.cbInclude, new CC());
                content.add(eftWrapper.jlName, new CC().wrap().gapX("8", "0"));
            }

            // Package Template
            SpoilerPane spoilerPane = new SpoilerPane(content, ExpandNode, CollapseNode, eptWrapper.getName());
            rootPanel.add(eptWrapper.cbInclude, new CC().alignY("top"));
            rootPanel.add(spoilerPane, new CC().pushX().growX().alignX("left").wrap().alignY("top").gapX("8", "0"));
        }
    }

    public JPanel getPanel() {
        return rootPanel;
    }

    public TextFieldWithBrowseButton getBtnPath() {
        return btnPath;
    }
}
