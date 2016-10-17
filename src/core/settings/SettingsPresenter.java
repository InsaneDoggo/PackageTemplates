package core.settings;

import global.utils.i18n.Language;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SettingsPresenter {

    void onPreShow();

    void onLanguageSelected(Language lang);
}
