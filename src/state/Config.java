package state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import state.models.StateWrapper;

/**
 * Created by CeH9 on 03.07.2016.
 */

@State(
        name = "PackageTemplateSettings",
        storages = {
                @Storage(file = "PackageTemplateSettings.xml")
        }
)
public class Config implements PersistentStateComponent<StateWrapper> {

    private StateWrapper myState;

    public StateWrapper getState() {
        return myState;
    }

    public void loadState(StateWrapper state) {
        myState = state;
    }
}
