package core.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 01.02.2017.
 */
public enum SearchActionType {
    @SerializedName("FILE") FILE("searchAction.File"),
    @SerializedName("DIR_ABOVE") DIR_ABOVE("searchAction.DirAbove"),
    @SerializedName("DIR_BELOW") DIR_BELOW("searchAction.DirBelow");

    private String nameLangKey;

    SearchActionType(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }

    public String getNameLangKey() {
        return nameLangKey;
    }
}
