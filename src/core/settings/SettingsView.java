package core.settings;

import core.state.models.UserSettings;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SettingsView {

    void setTitle(String title);
    void buildView(UserSettings userSettings);

}
