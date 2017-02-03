package core.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 01.02.2017.
 */
public class SearchAction {

    @Expose @SerializedName("actionType") private SearchActionType actionType;
    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("deepLimit") private int deepLimit;
    @Expose @SerializedName("isRegexp") private boolean isRegexp;

    public SearchAction(SearchActionType actionType, String name, int deepLimit, boolean isRegexp) {
        this.actionType = actionType;
        this.name = name;
        this.deepLimit = deepLimit;
        this.isRegexp = isRegexp;
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public SearchActionType getActionType() {
        return actionType;
    }

    public String getName() {
        return name;
    }

    public int getDeepLimit() {
        return deepLimit;
    }

    public boolean isRegexp() {
        return isRegexp;
    }
}
