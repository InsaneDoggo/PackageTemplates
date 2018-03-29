package core.textInjection;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 15.03.2017.
 */
public enum InjectDirection {
    @SerializedName("BEFORE") BEFORE("injectDirection.Before"),
    @SerializedName("AFTER") AFTER("injectDirection.After"),
    @SerializedName("PREV_LINE") PREV_LINE("injectDirection.PrevLine"),
    @SerializedName("NEXT_LINE") NEXT_LINE("injectDirection.NextLine"),
    @SerializedName("REPLACE") REPLACE("injectDirection.Replace"),
    @SerializedName("START_OF_FILE") START_OF_FILE("injectDirection.StartOfFile"),
    @SerializedName("SOF_END_OF_LINE") SOF_END_OF_LINE("injectDirection.SOF_END_OF_LINE"),
    @SerializedName("END_OF_FILE") END_OF_FILE("injectDirection.EndOfFile"),
    @SerializedName("EOF_START_OF_LINE") EOF_START_OF_LINE("injectDirection.EOF_START_OF_LINE");

    private String nameLangKey;

    InjectDirection(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }

    public String getNameLangKey() {
        return nameLangKey;
    }
}
