package core.settings;

import core.sync.BinaryFileConfig;
import core.writeRules.WriteRules;
import global.utils.i18n.Language;

import java.util.List;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SettingsPresenter {

    void onPreShow();

    void setAutoImport(List<String> list, WriteRules writeRules);

    void setLanguage(Language lang);

    void loadAutoImport();

    void addImportPath(List<String> oldPaths);

    void removeImportPath(List<String> paths, int pos);

    void loadBinaryFileConfig();

    void save();

    void setBinaryFilesConfig(BinaryFileConfig config);

}
