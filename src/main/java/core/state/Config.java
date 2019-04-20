package core.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import core.state.models.StateWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Created by CeH9 on 03.07.2016.
 */

@State(
        name = "PackageTemplateSettings_v0.2",
        storages = {
                @Storage(value = "PackageTemplateSettings_v0.2.xml")
        }
)
public class Config implements PersistentStateComponent<StateWrapper> {

    private StateWrapper myState;

    public StateWrapper getState() {
        return myState;
    }

    public void loadState(@NotNull StateWrapper state) {
        myState = state;
    }
}
