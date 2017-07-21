package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.IconUtil;
import com.intellij.util.PlatformIcons;
import core.actions.generated.BaseAction;
import core.state.util.SaveUtil;
import global.Const;
import global.listeners.ClickListener;
import global.models.Favourite;
import global.models.PackageTemplate;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileValidator;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateHelper;
import global.utils.templates.PackageTemplateHelper;
import global.views.FavouriteRadioButton;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends DialogWrapper implements SelectPackageTemplateView {

    private JPanel panel;
    private Project project;
    private SelectPackageTemplatePresenter presenter;

    protected SelectPackageTemplateDialog(@Nullable Project project) {
        super(project);
        presenter = new SelectPackageTemplatePresenterImpl(this, project);
        this.project = project;
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel(new MigLayout());
        new JBScrollPane(panel);
        panel.setMinimumSize(new Dimension(MIN_WIDTH, panel.getMinimumSize().height));

        makeToolBar();
        makePathButton();
        makeFavourites();

        return panel;
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    private static final int MIN_WIDTH = 600;
    private JComponent componentForValidation;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public void onCancel() {
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

    @Override
    protected void doHelpAction() {
        BrowserUtil.browse(Const.TUTORIALS_URL);
    }

    //=================================================================
    //  Validation
    //=================================================================
    private boolean skipValidation = false;
    protected final int VALIDATION_DELAY = 400;
    protected final int VALIDATION_HUGE_DELAY = 20 * 60 * 1000;  // 20 min

    protected void resetValidation() {
        skipValidation = true;
        initValidation();
    }

    @Override
    protected ValidationInfo doValidate() {
        if (skipValidation) {
            setValidationDelay(VALIDATION_HUGE_DELAY);
            skipValidation = false;
            return null;
        }

        if (selectedPath == null) {
            return presenter.doValidate(getSelectedPath(), btnPath);
        }

        return presenter.doValidate(getSelectedPath(), componentForValidation);
    }

    @Override
    protected void createDefaultActions() {
        super.createDefaultActions();
        myOKAction = new MyOkAction();
    }

    protected class MyOkAction extends OkAction {
        @Override
        protected void doAction(ActionEvent e) {
            setValidationDelay(VALIDATION_DELAY);
            initValidation();
            super.doAction(e);
        }
    }


    //=================================================================
    //  Toolbar
    //=================================================================
    private JPanel jpToolbar;
    private DefaultActionGroup actionGroup;

    private BaseAction actionAdd;
    private BaseAction actionEdit;
    private BaseAction actionSettings;
    private BaseAction actionExport;
    private BaseAction actionImport;
    private BaseAction actionAddToFavourites;
    private BaseAction actionAutoImport;

    private void makeToolBar() {
        actionGroup = new DefaultActionGroup();

        initToolbarActions();

        actionGroup.add(actionAdd);
        actionGroup.add(actionEdit);
        actionGroup.add(actionAddToFavourites);
        actionGroup.add(actionSettings);
        actionGroup.add(actionExport);
        actionGroup.add(actionImport);
        actionGroup.add(actionAutoImport);

        jpToolbar = new JPanel(new MigLayout());
        panel.add(jpToolbar, new CC().spanX());

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actionGroup, true);
        jpToolbar.add(toolbar.getComponent(), new CC().spanX());
    }

    private void initToolbarActions() {
        //Add
        actionAdd = new BaseAction(Localizer.get("tooltip.Create"), Localizer.get("description.CreateNewPackageTemplate"), IconUtil.getAddIcon()) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onAddAction();
            }
        };

        //Edit
        actionEdit = new BaseAction(Localizer.get("tooltip.Edit"), Localizer.get("description.EditExistingPackageTemplate"), IconUtil.getEditIcon()) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                if (FileValidator.isValidTemplatePath(getSelectedPath())) {
                    presenter.onEditAction(getSelectedPath());
                } else {
                    Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                }
            }
        };

        // Settings
        actionSettings = new BaseAction(Localizer.get("tooltip.Settings"), Localizer.get("description.EditGeneralSettings"), PlatformIcons.SHOW_SETTINGS_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onSettingsAction();
            }
        };

        // Export
        actionExport = new BaseAction(Localizer.get("tooltip.Export"), Localizer.get("description.ExportPackageTemplate") ,PlatformIcons.EXPORT_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                if (FileValidator.isValidTemplatePath(getSelectedPath())) {
                    presenter.onExportAction(getSelectedPath());
                } else {
                    Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                }
            }
        };

        // Import
        actionImport = new BaseAction(Localizer.get("tooltip.Import"), Localizer.get("description.ImportPackageTemplate"), PlatformIcons.IMPORT_ICON) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onImportAction();
            }
        };

        // Add to Favourite
        actionAddToFavourites = new BaseAction(Localizer.get("tooltip.Favourite"), Localizer.get("description.AddRemoveFromFavourites"), AllIcons.Toolwindows.ToolWindowFavorites) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                String path = getSelectedPath();
                switch (templateSourceType) {
                    case FAVOURITE:
                        presenter.removeFavourite(path);
                        break;
                    case PATH:
                        if (!FileValidator.isValidTemplatePath(path)) {
                            Messages.showErrorDialog(project, Localizer.get("warning.select.packageTemplate"), Localizer.get("title.SystemMessage"));
                            return;
                        }

                        presenter.addFavourite(path);
                        break;
                }
            }
        };

        // AutoImport
        actionAutoImport = new BaseAction(Localizer.get("tooltip.Auto"), Localizer.get("description.EditAutoImportSettings"), PluginIcons.AUTO_IMPORT) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                presenter.onAutoImportAction();
            }
        };

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

//        panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
        panel.add(btnPath, new CC().pushX().growX().spanX());
        addPathButtons();

        rbFromPath = new JBRadioButton(Localizer.get("label.FromPath"));
        rbFromPath.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                toggleSourcePath(
                        TemplateSourceType.PATH,
                        null,
                        btnPath
                );
            }
        });

        panel.add(rbFromPath, new CC().growX().spanX().wrap());
    }

    private void addPathButtons() {
        JButton btnDefaultPath = new JButton(Localizer.get("label.SetDefaultDirPath"));
        btnDefaultPath.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnPath.setText(PackageTemplateHelper.getRootDirPath());
            }
        });

        if (FileTemplateHelper.isDefaultScheme(project)) {
            panel.add(btnDefaultPath, new CC().spanX().wrap());
            return;
        } else {
            panel.add(btnDefaultPath, new CC().spanX().split(2));
        }

        JButton btnProjectPath = new JButton(Localizer.get("label.SetProjectDirPath"));
        btnProjectPath.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnPath.setText(PackageTemplateHelper.getProjectRootDirPath(project));
            }
        });

        panel.add(btnProjectPath, new CC().wrap());
    }

    @Override
    public void setPathBtnText(String path) {
        btnPath.setText(path);
    }

    @Override
    public void hideDialog() {
        doCancelAction();
    }

    //=================================================================
    //  Favourites
    //=================================================================
    private ButtonGroup buttonGroup;
    private ArrayList<FavouriteRadioButton> listButtons;
    private JPanel favouritesPanel;
    private JCheckBox cbCompactNames;
    private boolean isCompactNames = true;

    private void makeFavourites() {
        favouritesPanel = new JPanel(new MigLayout());
        buttonGroup = new ButtonGroup();
        listButtons = new ArrayList<>();
        buttonGroup.add(rbFromPath);
        panel.add(favouritesPanel, new CC().pushX().growX().spanX().wrap());

        buildFavouritesUI();
    }

    private void buildFavouritesUI() {
        resetFavourites();
        createFavouriteRadioButtons();

        if (!listButtons.isEmpty()) {
            cbCompactNames = new JCheckBox(Localizer.get("label.CompactNames"), isCompactNames);
            cbCompactNames.addItemListener(e -> {
                isCompactNames = e.getStateChange() == ItemEvent.SELECTED;
                buildFavouritesUI();
            });

            favouritesPanel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
            favouritesPanel.add(new JBLabel(Localizer.get("label.Favourites")), new CC().growX().spanX().pushX().wrap().alignX("center"));
            favouritesPanel.add(cbCompactNames, new CC().spanX().wrap().gapY("0", "10pt"));

            for (JBRadioButton radioButton : listButtons) {
                favouritesPanel.add(radioButton, new CC().growX().spanX().wrap());
            }

            listButtons.get(0).doClick();
        } else {
            rbFromPath.doClick();
        }
    }

    @Override
    public void updateFavouritesUI() {
        buildFavouritesUI();
        favouritesPanel.revalidate();
        pack();
    }

    @Override
    public void selectFavourite(Favourite selected) {
        if (listButtons == null && selected == null) {
            return;
        }

        for (FavouriteRadioButton item : listButtons) {
            if (item.getFavouritePath().equals(selected.getPath())) {
                item.doClick();
            }
        }
    }

    private void resetFavourites() {
        for (JBRadioButton btn : listButtons) {
            buttonGroup.remove(btn);
        }
        listButtons.clear();
        favouritesPanel.removeAll();
        favouritesPanel.revalidate();
    }

    private void createFavouriteRadioButtons() {
        ArrayList<Favourite> listFavourite = SaveUtil.reader().getListFavourite();
        listFavourite.sort(Comparator.comparingInt(Favourite::getOrder));

        ArrayList<Favourite> favouritesToDelete = new ArrayList<>();

        for (Favourite favourite : listFavourite) {
            File file = new File(favourite.getPath());
            if (!FileValidator.isTemplateFileValid(file)) {
                favouritesToDelete.add(favourite);
                continue;
            }

            PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(file);
            if (pt == null) {
                continue;
            }

            String favName = file.getName();
            if (!isCompactNames) {
                favName = file.getPath();
                if (favName.length() > Const.FAVOURITE_NAME_LIMIT) {
                    favName = "..." + favName.substring(favName.length() - (Const.FAVOURITE_NAME_LIMIT - 2), favName.length());
                }
            }

            FavouriteRadioButton radioButton = new FavouriteRadioButton(favName, PluginIcons.PACKAGE_TEMPLATES);
            radioButton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    toggleSourcePath(
                            TemplateSourceType.FAVOURITE,
                            favourite.getPath(),
                            radioButton
                    );
                }
            });
            radioButton.setFavouritePath(favourite.getPath());
            buttonGroup.add(radioButton);
            listButtons.add(radioButton);
        }

        // Delete Invalid templates
        for (Favourite favourite : favouritesToDelete) {
            SaveUtil.editor().removeFavourite(favourite);
        }
        SaveUtil.editor().save();
    }


    //=================================================================
    //  SourcePath
    //=================================================================
    private TemplateSourceType templateSourceType;

    private void toggleSourcePath(TemplateSourceType sourceType, String path, JComponent component) {
        templateSourceType = sourceType;
        selectedPath = path;
        componentForValidation = component;

        switch (sourceType) {
            case FAVOURITE:
//                actionAdd.setEnabled(false);
                break;
            case PATH:
//                actionAdd.setEnabled(true);
                break;
        }
    }


    //=================================================================
    //  Other
    //=================================================================
    private String getSelectedPath() {
        switch (templateSourceType) {
            case FAVOURITE:
                return selectedPath;
            case PATH:
                return btnPath.getText();
        }

        return selectedPath;
    }

}