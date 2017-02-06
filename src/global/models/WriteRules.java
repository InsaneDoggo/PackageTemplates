package global.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 11.09.2016.
 */
public enum WriteRules {
    @SerializedName("FROM_PARENT")FROM_PARENT,
    @SerializedName("ASK_ME")ASK_ME,
    @SerializedName("OVERWRITE")OVERWRITE,
    @SerializedName("USE_EXISTING")USE_EXISTING
}
