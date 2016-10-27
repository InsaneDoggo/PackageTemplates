package core.settings;

import core.state.util.SaveUtil;
import global.utils.i18n.Language;
import global.utils.i18n.Localizer;

/**
 * Created by Arsen on 16.09.2016.
 */
public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView view;

    public SettingsPresenterImpl(SettingsView view) {
        this.view = view;
    }

    @Override
    public void onPreShow() {
        view.setTitle(Localizer.get("title.Settings"));
        view.buildView();
    }

    @Override
    public void onLanguageSelected(Language lang) {
        SaveUtil.editor()
                .setLanguage(lang)
                .save();
    }
}
