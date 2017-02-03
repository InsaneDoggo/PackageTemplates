package core.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 01.02.2017.
 */
public enum SearchActionType {
    @SerializedName("FILE") FILE("search_action.file"),
    @SerializedName("DIR_ABOVE") DIR_ABOVE("search_action.dir_above"),
    @SerializedName("DIR_BELOW") DIR_BELOW("search_action.dir_below");

    private String nameLangKey;

    SearchActionType(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }
}
