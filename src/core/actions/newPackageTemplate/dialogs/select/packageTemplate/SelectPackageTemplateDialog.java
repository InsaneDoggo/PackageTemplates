package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.history.LocalHistory;
import com.intellij.history.LocalHistoryAction;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.DeleteHandler;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.impl.UndoManagerImpl;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.ui.playback.commands.KeyStrokeMap;
import com.intellij.psi.PsiElement;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.IconUtil;
import com.intellij.util.PlatformIcons;
import core.actions.newPackageTemplate.Tester;
import core.state.util.SaveUtil;
import global.models.Favourite;
import global.models.PackageTemplate;
import global.utils.FileReaderUtil;
import global.utils.FileValidator;
import global.utils.PackageTemplateHelper;
import global.utils.i18n.Localizer;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends DialogWrapper implements SelectPackageTemplateView {

    private static final int MIN_WIDTH = 600;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onCancel();

    private JPanel panel;
    private Project project;
    private SelectPackageTemplatePresenter presenter;
    private JComponent componentForValidation;

    protected SelectPackageTemplateDialog(@Nullable Project project) {
        super(project);
        presenter = new SelectPackageTemplatePresenterImpl(this, project);
        this.project = project;
        init();
        setValidationDelay(1000);
    }

    @Override
    protected ValidationInfo doValidate() {
        if (selectedPath == null) {
            return presenter.doValidate(getSelectedPath(), btnPath);
        }

        return presenter.doValidate(getSelectedPath(), componentForValidation);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case DialogWrapper.OK_EXIT_CODE:
                presenter.onSuccess(PackageTemplateHelper.getPackageTemplate(getSelectedPath()));
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

//    @Nullable
//    @Override
//    public JComponent getPreferredFocusedComponent() {
//        return btnPath;
//    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel(new MigLayout());
        new JBScrollPane(panel);
        panel.setMinimumSize(new Dimension(MIN_WIDTH, panel.getMinimumSize().height));

        initShortcuts();

        makeToolBar();
        makePathButton();
        makeFavourites();

        return panel;
    }

//    @Override
//    public void update(AnActionEvent e) {
//        super.update(e);
//        e.getPresentation().setEnabled(canUndo());
//    }

    private void initShortcuts() {
        // Test
        Keymap activeKeymap = KeymapManager.getInstance().getActiveKeymap();
        Shortcut[] undoShortcuts = activeKeymap.getShortcuts(IdeActions.ACTION_UNDO);

        AnAction undoAction = new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent e) {
                undo();
            }
        };

        undoAction.registerCustomShortcutSet(new CustomShortcutSet(undoShortcuts), getRootPane());
    }

    private void undo() {
        System.out.println("undo ");
//        if (myUndoManager.canUndo()) {
//            myUndoManager.undo();
//        }
    }
    private boolean canUndo() {
        System.out.println("canUndo ");
        return true;
    }

    private JPanel jpToolbar;
    private DefaultActionGroup actionGroup;

    private AnAction actionAdd;
    private AnAction actionEdit;
    private AnAction actionSettings;
    private AnAction actionExport;
    private AnAction actionAddToFavourites;

    private String getSelectedPath() {
        if (selectedPath == null) {
            return btnPath.getText();
        }

        return selectedPath;
    }

    private void makeToolBar() {
        actionGroup = new DefaultActionGroup();

        actionAdd = new AnAction(IconUtil.getAddIcon()) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onAddAction();
            }
        };
        actionEdit = new AnAction(IconUtil.getEditIcon()) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                if (FileValidator.isValidTemplatePath(getSelectedPath())) {
                    presenter.onEditAction(getSelectedPath());
                } else {
                    Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                }
            }
        };
        actionSettings = new AnAction(PlatformIcons.SHOW_SETTINGS_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                new Tester().runMockAction(project);
                //presenter.onSettingsAction();
            }
        };
        actionExport = new AnAction(PlatformIcons.EXPORT_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                if (FileValidator.isValidTemplatePath(getSelectedPath())) {
                    presenter.onExportAction(getSelectedPath());
                } else {
                    Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                }
            }
        };
        actionAddToFavourites = new AnAction(AllIcons.Toolwindows.ToolWindowFavorites) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                String path = btnPath.getText();
                if (!FileValidator.isValidTemplatePath(path)) {
                    Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                    return;
                }

                presenter.onAddToFavourites(path);
            }
        };
        //todo add favourite

        actionGroup.add(actionAdd);
        actionGroup.add(actionEdit);
        actionGroup.add(actionAddToFavourites);
        actionGroup.add(actionSettings);
        actionGroup.add(actionExport);

        jpToolbar = new JPanel(new MigLayout());
        panel.add(jpToolbar, new CC().spanX().wrap());

        refreshToolbar();
    }

    private void refreshToolbar() {
        jpToolbar.removeAll();
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actionGroup, true);
        jpToolbar.add(toolbar.getComponent(), new CC().spanX().wrap());
    }


    //=================================================================
    //  From path
    //=================================================================
    private TextFieldWithBrowseButton btnPath;
    private JBRadioButton rbFromPath;
    private String selectedPath;

    private void makePathButton() {
        btnPath = new TextFieldWithBrowseButton();
        btnPath.setText(PackageTemplateHelper.getRootDirPath());
        btnPath.addBrowseFolderListener(Localizer.get("SelectPackageTemplate"), "", project, FileReaderUtil.getPackageTemplatesDescriptor());

        panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
        panel.add(btnPath, new CC().pushX().growX().spanX());

        rbFromPath = new JBRadioButton(Localizer.get("label.FromPath"));
        rbFromPath.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actionAdd.getTemplatePresentation().setEnabled(true);
                actionAddToFavourites.getTemplatePresentation().setEnabled(true);
                selectedPath = null;
                componentForValidation = btnPath;
                refreshToolbar();

            }
        });
        panel.add(rbFromPath, new CC().growX().spanX().wrap());
    }


    //=================================================================
    //  Favourites
    //=================================================================
    private ButtonGroup buttonGroup;
    private ArrayList<JBRadioButton> listButtons;

    private void makeFavourites() {
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbFromPath);

        ArrayList<Favourite> listFavourite = SaveUtil.reader().getListFavourite();
        listFavourite.sort((o1, o2) -> o1.getOrder() - o2.getOrder());
        listButtons = new ArrayList<>();

        for (Favourite favourite : listFavourite) {
            File file = new File(favourite.getPath());
            if (!FileValidator.isTemplateFileValid(file)) {
                continue;
            }

            PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(file);
            if (pt == null) {
                continue;
            }

            JBRadioButton radioButton = new JBRadioButton(PackageTemplateHelper.createNameFromPath(file), PluginIcons.PACKAGE_TEMPLATES);
            radioButton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    actionAdd.getTemplatePresentation().setEnabled(false);
                    actionAddToFavourites.getTemplatePresentation().setEnabled(false);
                    selectedPath = favourite.getPath();
                    componentForValidation = radioButton;
                    refreshToolbar();
                }
            });
            buttonGroup.add(radioButton);
            listButtons.add(radioButton);
        }

        if (!listButtons.isEmpty()) {
            panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
            panel.add(new JBLabel(Localizer.get("label.Favourites")), new CC().growX().spanX().pushX().wrap().alignX("center"));
            for (JBRadioButton radioButton : listButtons) {
                panel.add(radioButton, new CC().growX().spanX().wrap());
            }

            listButtons.get(0).doClick();
        } else {
            rbFromPath.doClick();
        }
    }

}