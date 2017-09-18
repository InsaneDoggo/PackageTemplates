package core.actions.newPackageTemplate.dialogs.select.binaryFile;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBScrollPane;
import global.models.BinaryFile;
import global.utils.Logger;
import global.utils.factories.GsonFactory;
import global.utils.file.FileReaderUtil;
import global.utils.i18n.Localizer;
import global.utils.templates.PackageTemplateHelper;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectBinaryFileDialog extends DialogWrapper implements SelectBinaryFileView {

    private SelectBinaryFilePresenter presenter;
    private Project project;
    @Nullable
    private BinaryFile binaryFile;

    public SelectBinaryFileDialog(Project project, @Nullable BinaryFile binaryFile) {
        super(project);
        this.project = project;
        this.binaryFile = GsonFactory.cloneObject(binaryFile, BinaryFile.class);
        init();
    }


    //=================================================================
    //  UI
    //=================================================================
    private JPanel panel;
    private TextFieldWithBrowseButton btnPath;
    private ButtonGroup bgSourceMode;
    private JBRadioButton rbCopyToLib;
    private JBRadioButton rbUseOriginal;

    private void render() {
        btnPath = new TextFieldWithBrowseButton();

        if (binaryFile == null) {
            binaryFile = BinaryFile.newInstance();
            binaryFile.setSourcePath(PackageTemplateHelper.getRootDirPath());
        }

        btnPath.setText(binaryFile.getSourcePath());
        btnPath.addBrowseFolderListener(Localizer.get("library.ChooseBinaryFile"), "", project, FileReaderUtil.getBinaryFileDescriptor());

        panel.add(btnPath, new CC().wrap().pushX().growX());
    }

    protected void collectDataFromFields() {
        binaryFile.setSourcePath(btnPath.getText());
    }

    @Override
    protected ValidationInfo doValidate() {
        // Path
        File file = new File(btnPath.getText());
        if (btnPath.getText().isEmpty() || !file.exists() || file.isDirectory()) {
            return new ValidationInfo(Localizer.get("warning.WrongFilePath"), btnPath);
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        collectDataFromFields();
        onSuccess(binaryFile);
    }

    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    @Override
    protected JComponent createCenterPanel() {
        presenter = new SelectBinaryFilePresenterImpl(this);
        panel = new JPanel();
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        render();

        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setMinimumSize(new Dimension(600, 200));
        return scrollPane;
    }

}
