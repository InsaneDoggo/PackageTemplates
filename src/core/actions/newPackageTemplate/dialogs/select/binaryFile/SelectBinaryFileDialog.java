package core.actions.newPackageTemplate.dialogs.select.binaryFile;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBScrollPane;
import core.actions.newPackageTemplate.dialogs.implement.ImplementDialog;
import global.utils.file.FileReaderUtil;
import global.utils.i18n.Localizer;
import global.utils.templates.PackageTemplateHelper;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectBinaryFileDialog extends DialogWrapper implements SelectBinaryFileView {

    private SelectBinaryFilePresenter presenter;
    private Project project;

    public SelectBinaryFileDialog(Project project) {
        super(project);
        this.project = project;
        init();
    }


    //=================================================================
    //  UI
    //=================================================================
    private JPanel panel;
    private TextFieldWithBrowseButton btnPath;
    private ButtonGroup bgSourceMode;

    private void render() {
        JLabel jlDescription = new JLabel(Localizer.get("Description"));

        btnPath = new TextFieldWithBrowseButton();
        btnPath.setText(PackageTemplateHelper.getRootDirPath());
        btnPath.addBrowseFolderListener(Localizer.get("title.SelectBinaryFile"), "", project, FileReaderUtil.getBinaryFileDescriptor());

        JBRadioButton rbCopyToDir;
        JBRadioButton rbUseOriginal;

        panel.add(btnPath, new CC().wrap().pushX().growX());
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    public abstract void onSuccess();

    public abstract void onCancel();

    @Override
    protected JComponent createCenterPanel() {
        presenter = new SelectBinaryFilePresenterImpl(this);
        panel = new JPanel();
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        render();

        return new JBScrollPane(panel);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case ImplementDialog.OK_EXIT_CODE:
                presenter.onSuccess();
                break;
            case ImplementDialog.CANCEL_EXIT_CODE:
                presenter.onCancel();
                break;
        }
    }

}
