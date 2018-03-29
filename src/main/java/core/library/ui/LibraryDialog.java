package core.library.ui;

import base.SimpleBaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBColor;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTabbedPane;
import core.library.models.lib.BinaryFileLibModel;
import global.utils.file.FileReaderUtil;
import global.utils.i18n.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by CeH9 on 22.06.2016.
 */

public class LibraryDialog extends SimpleBaseDialog {

    LibraryPresenter presenter;

    public LibraryDialog(Project project) {
        super(project);
        presenter = new LibraryPresenter(this);
    }


    //=================================================================
    //  UI
    //=================================================================
    private JTabbedPane tabbedPane;
    private JPanel pTabBinaryFiles;
    private JPanel pRight;

    public void buildView() {
        setTitle(Localizer.get("title.LibraryDialog"));
        panel.setBackground(JBColor.BLUE);

        tabbedPane = new JBTabbedPane();
        panel.add(tabbedPane, new CC().push().grow());

        initBinaryFilesTab();
        initSecondTab();
    }

    //=================================================================
    //  BinaryFiles Tab
    //=================================================================
    private JBList<Pair<String, BinaryFileLibModel>> listBinaryFiles;
    private TextFieldWithBrowseButton btnBinaryFilePath;
    private EditorTextField tfBinaryFileAlias;

    private void initBinaryFilesTab() {
        pTabBinaryFiles = new JPanel(new MigLayout(new LC()));

        // List
        createBinaryFileList();

        //Content (right panel)
        JPanel panelBinaryFilesContent = new JPanel(new MigLayout());
        btnBinaryFilePath = new TextFieldWithBrowseButton();
        btnBinaryFilePath.setText("");
        btnBinaryFilePath.addBrowseFolderListener(Localizer.get("library.ChooseBinaryFile"), "", project, FileReaderUtil.getBinaryFileDescriptor());

        tfBinaryFileAlias = new EditorTextField("ExampleAlias");

        panelBinaryFilesContent.add(tfBinaryFileAlias, new CC().wrap());
        panelBinaryFilesContent.add(btnBinaryFilePath, new CC().wrap().gapY("8pt", "0"));

        //Splitter
        JBSplitter splitter = new JBSplitter(0.3f);
        splitter.setFirstComponent(listBinaryFiles);
        splitter.setSecondComponent(panelBinaryFilesContent);

        pTabBinaryFiles.add(splitter, new CC().push().grow());
        tabbedPane.addTab("Tab1", pTabBinaryFiles);
    }

    private void createBinaryFileList() {
        ArrayList<Pair<String, BinaryFileLibModel>> libModels = new ArrayList<>();
        libModels.add(new Pair<>("key1", new BinaryFileLibModel("C:/foo/bar/path1")));
        libModels.add(new Pair<>("key2", new BinaryFileLibModel("C:/foo/bar/path2")));
        libModels.add(new Pair<>("key3", new BinaryFileLibModel("C:/foo/bar/path3")));


        listBinaryFiles = new JBList<>(libModels);
        listBinaryFiles.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            return new JBLabel(value.first);
        });
        listBinaryFiles.addListSelectionListener(arg0 -> {
            if (!arg0.getValueIsAdjusting()) {
                presenter.onBinaryFileSelected(listBinaryFiles.getSelectedValue());
            }
        });

    }

    public void setBinaryFileTabContent(Pair<String, BinaryFileLibModel> value) {
        tfBinaryFileAlias.setText(value.first);
        btnBinaryFilePath.setText(value.second.getPath());
    }


    //=================================================================
    //  Second Tab
    //=================================================================
    private void initSecondTab() {
        pRight = new JPanel(new MigLayout(new LC()));
        tabbedPane.addTab("Tab2", pRight);
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    @Override
    protected MigLayout createLayout() {
        return new MigLayout(new LC()
                .insets("0")
                .fill()
                .gridGap("0", "0")
        );
    }

    @Override
    protected void onPositiveButtonClick() {

    }

    @Override
    protected void collectDataFromUI() {

    }

    @Override
    protected boolean isDataValid() {
//        Messages.showErrorDialog(Localizer.get("warning.example"), "Example");
        return false;
    }

    @Override
    protected Dimension getMinSizeDimension() {
        return new Dimension(768, 256);
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action cancelAction = getCancelAction();
        Action okAction = getOKAction();

        cancelAction.putValue(Action.NAME, Localizer.get("action.Cancel"));
        okAction.putValue(Action.NAME, Localizer.get("action.Save"));

        return new Action[]{okAction, cancelAction};
    }

}













