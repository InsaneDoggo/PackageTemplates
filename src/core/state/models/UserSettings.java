package core.state.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import global.utils.i18n.Language;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Arsen on 10.09.2016.
 */
public class UserSettings {

    @Expose @SerializedName("language") private Language language;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

}
