package core.settings;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.SeparatorComponent;
import com.intellij.util.IconUtil;
import core.state.util.SaveUtil;
import core.textInjection.dialog.TextInjectionWrapper;
import core.writeRules.WriteRules;
import core.writeRules.dialog.WriteRulesCellRenderer;
import global.listeners.ClickListener;
import global.utils.file.FileReaderUtil;
import global.utils.i18n.Language;
import global.utils.i18n.Localizer;
import global.views.adapter.ListView;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.plugin2.message.Message;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by CeH9 on 22.06.2016.
 */

public class SettingsDialog extends BaseDialog implements SettingsView {

    private SettingsPresenter presenter;
    private JLabel jlLanguage;
    private ComboBox comboLanguages;

    public SettingsDialog(Project project) {
        super(project);
        presenter = new SettingsPresenterImpl(this);
    }


    //=================================================================
    //  UI
    //=================================================================
    @Override
    public void buildView() {
        panel.setLayout(new MigLayout(new LC().insets("0").fillX().gridGapY("1pt")));

        buildLanguageBlock();
        buildAutoImportBlock();
    }

    private void buildLanguageBlock() {
        createLanguageViews();

        // No RU support yet
        //todo enable lang selection
        jlLanguage.setEnabled(false);
        comboLanguages.setEnabled(false);

        panel.add(jlLanguage, new CC().spanX().split(2));
        panel.add(comboLanguages, new CC().wrap());
    }


    //=================================================================
    //  AutoImport
    //=================================================================
    private ListView<String> lvAutoImport;
    private List<String> paths;
    private ComboBox comboBoxRules;

    private void buildAutoImportBlock() {
        panel.add(new SeparatorComponent(10), new CC().pushX().growX().wrap().spanX());
        panel.add(new JLabel(Localizer.get("settings.AutoImport"), JLabel.CENTER), new CC().wrap().growX().pushX().spanX());

        //Type
        ArrayList<WriteRules> actionTypes = new ArrayList<>();
        actionTypes.add(WriteRules.USE_EXISTING);
        actionTypes.add(WriteRules.ASK_ME);
        actionTypes.add(WriteRules.OVERWRITE);

        comboBoxRules = new ComboBox(actionTypes.toArray());
        comboBoxRules.setRenderer(new WriteRulesCellRenderer());

        panel.add(comboBoxRules, new CC().pushX().growX().spanX().gapY("10","10"));

        paths = new ArrayList<>();
        lvAutoImport = new ListView<String>(paths) {
            @Nullable
            @Override
            public CollectDataFromUI onBuildView(String item, int pos) {
                TextFieldWithBrowseButton btnPath = new TextFieldWithBrowseButton();
                btnPath.setText(item);
                btnPath.addBrowseFolderListener(Localizer.get("SelectDirectoryToImport"), "", project, FileReaderUtil.getDirectoryDescriptor());

                JButton btnDelete = new JButton(IconUtil.getRemoveIcon());
                btnDelete.addMouseListener(new ClickListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        lvAutoImport.collectDataFromUI();
                        presenter.removeImportPath(paths, pos);
                    }
                });

                add(btnDelete, new CC().split(2).spanX());
                add(btnPath, new CC().pushX().growX());
                return () -> list.set(pos, btnPath.getText());
            }
        };
        panel.add(lvAutoImport, new CC().spanX().pushX().growX().wrap());

        JButton btnAdd = new JButton(Localizer.get("action.AddPath"), IconUtil.getAddIcon());
        btnAdd.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lvAutoImport.collectDataFromUI();
                presenter.addImportPath(paths);
            }
        });
        panel.add(btnAdd, new CC().wrap());

        presenter.loadAutoImport();
    }

    @Override
    public void buildAutoImport(List<String> list) {
        paths.clear();
        paths.addAll(list);
        lvAutoImport.reBuildUI();
        pack();
    }

    @Override
    public void setAutoImportWriteRules(WriteRules writeRules) {
        comboBoxRules.setSelectedItem(writeRules);
    }

    private void createLanguageViews() {
        jlLanguage = new JLabel(Localizer.get("settings.Language"));

        comboLanguages = new ComboBox(new EnumComboBoxModel<>(Language.class));
        if (SaveUtil.reader().getLanguage() != null) {
            comboLanguages.setSelectedItem(SaveUtil.reader().getLanguage());
        }
    }


    //=================================================================
    //  Save
    //=================================================================
    public void saveAll() {
        //Language
        //todo language save
//        Language selectedLang = (Language) comboLanguages.getSelectedItem();
//        presenter.saveLanguage(selectedLang);

        //AutoImport
        presenter.saveAutoImport(paths, (WriteRules) comboBoxRules.getSelectedItem());
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    @Override
    public void preShow() {
        presenter.onPreShow();
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


    //=================================================================
    //  Validate
    //=================================================================
    @Override
    protected void doOKAction() {
        if (!isDataValid()) {
            return;
        }

        saveAll();
        super.doOKAction();
    }

    private boolean isDataValid() {
        lvAutoImport.collectDataFromUI();

        for(String path : paths){
            if(path.trim().isEmpty()){
                Messages.showErrorDialog(Localizer.get("warning.PathShoudntBeEmpty"), "AutoImport");
                return false;
            }
        }

        return true;
    }

}













