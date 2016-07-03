package state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

/**
 * Created by CeH9 on 03.07.2016.
 */

@State(
        name = "PackageTemplateSettings",
        storages = {
                @Storage(file = "PackageTemplateSettings.xml")
        }
)
public class Config implements PersistentStateComponent<PackageTemplateState> {

    private PackageTemplateState myState;

    public PackageTemplateState getState() {
        return myState;
    }

    public void loadState(PackageTemplateState state) {
        myState = state;
    }
}
