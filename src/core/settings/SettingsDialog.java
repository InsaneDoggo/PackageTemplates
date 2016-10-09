package core.settings;

import base.BaseDialog;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CommonActionsPanel;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBComboBoxLabel;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.PlatformIcons;
import core.state.util.SaveUtil;
import global.utils.i18n.Language;
import global.utils.i18n.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.codec.language.bm.Lang;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by CeH9 on 22.06.2016.
 */

public class SettingsDialog extends BaseDialog implements SettingsView {

    private SettingsPresenter presenter;

    public SettingsDialog(Project project) {
        super(project);
        presenter = new SettingsPresenterImpl(this);
    }


    @Override
    public void preShow() {
        presenter.onPreShow();
    }

    @Override
    public void buildView() {
        panel.setLayout(new MigLayout());

        // Language
        JLabel jlLanguage = new JLabel(Localizer.get("settings.Language"));
//
//        ArrayList<String> languages = new ArrayList<>();
//        for(Language lang: Language.values()){
//            languages.add(lang.toString());
//        }
        JComboBox<Language> comboLanguages = new ComboBox<>(new EnumComboBoxModel<>(Language.class));
        Language userLang = SaveUtil.getInstance().getStateModel().getUserSettings().getLanguage();
        if (userLang != null) {
            comboLanguages.setSelectedItem(userLang);
        }

        comboLanguages.addItemListener(e -> {
            Language selectedLang = (Language) comboLanguages.getSelectedItem();
            SaveUtil.getInstance().editor()
                    .setLanguage(selectedLang)
                    .save();
        });

        panel.add(jlLanguage, new CC());
        panel.add(comboLanguages, new CC().wrap());
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action cancelAction = getCancelAction();
        cancelAction.putValue(Action.NAME, Localizer.get("action.Done"));
        return new Action[]{cancelAction};
    }
}
