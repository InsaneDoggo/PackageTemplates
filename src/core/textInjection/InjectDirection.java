package core.textInjection;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 15.03.2017.
 */
public enum InjectDirection {
    @SerializedName("BEFORE") BEFORE("injectDirection.Before"),
    @SerializedName("AFTER") AFTER("injectDirection.After"),
    @SerializedName("REPLACE") REPLACE("injectDirection.Replace"),
    @SerializedName("START_OF_FILE") START_OF_FILE("injectDirection.StartOfFile"),
    @SerializedName("END_OF_FILE") END_OF_FILE("injectDirection.EndOfFile");

    private String nameLangKey;

    InjectDirection(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }

    public String getNameLangKey() {
        return nameLangKey;
    }
}
