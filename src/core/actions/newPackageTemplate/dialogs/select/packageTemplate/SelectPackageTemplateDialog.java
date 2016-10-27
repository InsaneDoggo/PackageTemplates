package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CommonActionsPanel;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.tabs.impl.ActionPanel;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.IconUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.containers.HashMap;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.tree.PackageTemplateCellRender;
import core.state.util.SaveUtil;
import global.Const;
import global.listeners.ClickListener;
import global.models.PackageTemplate;
import global.utils.FileReaderUtil;
import global.utils.PackageTemplateHelper;
import global.utils.i18n.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends DialogWrapper implements SelectPackageTemplateView {

    private static final int MIN_WIDTH = 600;
    private static final int MIN_HEIGHT = 520;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onCancel();

    private JPanel panel;
    private Project project;
    private SelectPackageTemplatePresenter presenter;
    private TextFieldWithBrowseButton btnPath;

    protected SelectPackageTemplateDialog(@Nullable Project project) {
        super(project);
        presenter = new SelectPackageTemplatePresenterImpl(this, project);
        this.project = project;
        init();
        setValidationDelay(1000);
    }

    @Override
    protected ValidationInfo doValidate() {
        return presenter.doValidate(btnPath.getText(), btnPath);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case DialogWrapper.OK_EXIT_CODE:
                presenter.onSuccess(PackageTemplateHelper.getPackageTemplate(btnPath.getText()));
                break;
            case DialogWrapper.CANCEL_EXIT_CODE:
                presenter.onCancel();
                break;
        }
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action action = super.getOKAction();
        action.putValue(Action.NAME, Localizer.get("action.Select"));
        return action;
    }

    @NotNull
    @Override
    protected Action getCancelAction() {
        Action action = super.getCancelAction();
        action.putValue(Action.NAME, Localizer.get("action.Cancel"));
        return action;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return btnPath;
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel(new MigLayout());
        panel.setMinimumSize(new Dimension(MIN_WIDTH, panel.getMinimumSize().height));

        makeToolBar();
        makePathButton();
        makeFavourites();

        return panel;
    }

    private void makeFavourites() {
        JBLabel jlFavourites = new JBLabel(Localizer.get("label.Favourites"));

        panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
//        panel.add()
    }

    private void makePathButton() {
        btnPath = new TextFieldWithBrowseButton();
        btnPath.setText(PackageTemplateHelper.getRootDirPath());
        btnPath.addBrowseFolderListener(Localizer.get("SelectPackageTemplate"), "", project, FileReaderUtil.getPackageTemplatesDescriptor());

        panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
        panel.add(btnPath, new CC().pushX().growX().spanX());
    }

    private void makeToolBar() {
        DefaultActionGroup actions = new DefaultActionGroup();

        AnAction actionAdd = new AnAction(IconUtil.getAddIcon()) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onAddAction();
            }
        };
        AnAction actionEdit = new AnAction(IconUtil.getEditIcon()) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                String path = btnPath.getText();
                if (path != null && !path.isEmpty() && !(new File(path).isDirectory())) {
                    presenter.onEditAction(PackageTemplateHelper.getPackageTemplate(btnPath.getText()));
                } else {
                    Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                }
            }
        };
        AnAction actionSettings = new AnAction(PlatformIcons.SHOW_SETTINGS_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onSettingsAction();
            }
        };
        AnAction actionExport = new AnAction(PlatformIcons.EXPORT_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onExportAction();
            }
        };

        actions.add(actionAdd);
        actions.add(actionEdit);
        actions.add(actionSettings);
//        actions.add(actionExport);

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actions, true);

        panel.add(toolbar.getComponent(), new CC().spanX().wrap());
    }

}
