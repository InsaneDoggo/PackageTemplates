package core.settings;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.SeparatorComponent;
import com.intellij.util.IconUtil;
import core.state.util.SaveUtil;
import global.listeners.ClickListener;
import global.utils.i18n.Language;
import global.utils.i18n.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;

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

    @Override
    public void buildView() {
        panel.setLayout(new MigLayout());

        createLanguageViews();

        // No RU support yet
        //todo enable lang selection
        jlLanguage.setEnabled(false);
        comboLanguages.setEnabled(false);

        panel.add(jlLanguage, new CC());
        panel.add(comboLanguages, new CC().wrap());

        // AutoImport
        panel.add(new SeparatorComponent(10), new CC().pushX().growX().wrap().spanX());
        JLabel jlTextInjection = new JLabel(Localizer.get("settings.AutoImport"), JLabel.CENTER);
        panel.add(jlTextInjection, new CC().wrap().growX().pushX().spanX());

        JButton btnAdd = new JButton(Localizer.get("action.AddPath"), IconUtil.getAddIcon());
        btnAdd.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addPathToImport();
            }
        });
        panel.add(btnAdd, new CC().wrap());
    }

    private void addPathToImport() {

    }

    private void createLanguageViews() {
        jlLanguage = new JLabel(Localizer.get("settings.Language"));

        comboLanguages = new ComboBox(new EnumComboBoxModel<>(Language.class));
        if (SaveUtil.reader().getLanguage() != null) {
            comboLanguages.setSelectedItem(SaveUtil.reader().getLanguage());
        }

        comboLanguages.addItemListener(e -> {
            Language selectedLang = (Language) comboLanguages.getSelectedItem();
            presenter.onLanguageSelected(selectedLang);
        });
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
        cancelAction.putValue(Action.NAME, Localizer.get("action.Done"));
        return new Action[]{cancelAction};
    }

}













