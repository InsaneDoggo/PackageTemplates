package io;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

/**
 * Created by CeH9 on 03.07.2016.
 */

@State(
        name = "PackageTemplateSettings",
        storages = {
                @Storage(file = "$WORKSPACE_FILE$")
        }
)
public class COnfig implements PersistentStateComponent<PluginState> {

    private static COnfig instance;

    public static PluginState getInstance() {
        if (instance == null) {
            instance = new COnfig();
            instance.load();
        }

        return instance;
    }

    @Nullable
    @Override
    public PluginState getState() {
        return null;
    }

    @Override
    public void loadState(PluginState pluginState) {

    }
}
