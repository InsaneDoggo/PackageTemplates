package core.settings;

import core.sync.BinaryFileConfig;
import core.writeRules.WriteRules;

import java.util.List;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SettingsView {

    void setTitle(String title);
    void buildView();
    void buildAutoImport(List<String> paths);
    void setAutoImportWriteRules(WriteRules writeRules);
    void buildBinaryFilesBlock(BinaryFileConfig config);

}
