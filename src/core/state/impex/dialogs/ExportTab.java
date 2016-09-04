package core.state.impex.dialogs;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import core.state.impex.models.ExpFileTemplateWrapper;
import core.state.impex.models.ExpPackageTemplateWrapper;
import global.utils.Localizer;
import global.views.SpoilerPane;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.intellij.icons.AllIcons.Nodes.CollapseNode;
import static com.intellij.icons.AllIcons.Nodes.ExpandNode;

/**
 * Created by Arsen on 04.09.2016.
 */
public class ExportTab {

    private Project project;
    private ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper;

    public ExportTab(Project project, ArrayList<ExpPackageTemplateWrapper> listExpPackageTemplateWrapper) {
        this.project = project;
        this.listExpPackageTemplateWrapper = listExpPackageTemplateWrapper;

        init();
    }

    private JPanel rootPanel;
    private TextFieldWithBrowseButton btnPath;

    public TextFieldWithBrowseButton getBtnPath() {
        return btnPath;
    }

    private void init() {
        rootPanel = new JPanel(new MigLayout(new LC().fillX()));
        rootPanel.setMinimumSize(new Dimension(480, 480));

        btnPath = new TextFieldWithBrowseButton();
        btnPath.addBrowseFolderListener(Localizer.get("ExportTemplatesTo"), "", project,
                new FileChooserDescriptor(false, true, false, false, false, false));

        for (ExpPackageTemplateWrapper eptWrapper : listExpPackageTemplateWrapper) {
            // File templates
            JPanel content = new JPanel(new MigLayout());
            for (ExpFileTemplateWrapper eftWrapper : eptWrapper.getListExpFileTemplateWrapper()) {
                content.add(eftWrapper.cbInclude, new CC());
                content.add(eftWrapper.jlName, new CC().wrap().gapX("8", "0"));
            }

            // Package Template
            SpoilerPane spoilerPane = new SpoilerPane(content, ExpandNode, CollapseNode, eptWrapper.getName());
            rootPanel.add(eptWrapper.cbInclude, new CC().alignY("top"));
            rootPanel.add(spoilerPane, new CC().pushX().growX().alignX("left").wrap().alignY("top").gapX("8", "0"));
        }

        rootPanel.add(btnPath, new CC().growX().spanX().wrap().gapY("12", "12"));
    }

    public JPanel getPanel() {
        return rootPanel;
    }
}
