package custom.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.TabbedPaneImpl;
import com.intellij.util.ui.GridBag;
import icons.VcsLogIcons;
import org.jetbrains.annotations.Nullable;
import state.SaveUtil;
import utils.FileWriter;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 04.07.2016.
 */

public abstract class ImpexDialog extends DialogWrapper {

    public abstract void onSuccess();

    public abstract void onCancel();

    private Project project;
    private TextFieldWithBrowseButton tfbButton;
    private TabbedPaneImpl tabContainer;

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
            return new ValidationInfo("Fill empty fields");
        }

        return null;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        tabContainer = new TabbedPaneImpl(SwingConstants.TOP);
        tabContainer.setKeyboardNavigation(TabbedPaneImpl.DEFAULT_PREV_NEXT_SHORTCUTS);

        JPanel panelExport = new JPanel();
        panelExport.setMinimumSize(new Dimension(250, 150));
        panelExport.setLayout(new GridBagLayout());

        tfbButton = new TextFieldWithBrowseButton();
        tfbButton.addBrowseFolderListener("Export templates to", "", project,
                new FileChooserDescriptor(false, true, false, false, false, false));

        GridBag bag = UIMaker.getDefaultGridBag();

        panelExport.add(tfbButton, bag.nextLine().next());

        tabContainer.addTab("Export", AllIcons.General.ExportSettings, panelExport);
        tabContainer.addTab("Import", AllIcons.General.ImportSettings, new JPanel());
        return tabContainer;
    }

}
