package core.settings;

import core.writeRules.WriteRules;
import global.utils.i18n.Language;

import java.util.List;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SettingsPresenter {

    void onPreShow();

    void saveAutoImport(List<String> list, WriteRules writeRules);

    void saveLanguage(Language lang);

    void loadAutoImport();

    void addImportPath(List<String> oldPaths);

    void removeImportPath(List<String> paths, int pos);
}
