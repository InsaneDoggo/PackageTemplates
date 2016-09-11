package global.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 11.09.2016.
 */
public enum WriteBehavior {
    @SerializedName("1")CREATE_IF_NOT_EXIST,
    @SerializedName("2")USE_EXISTING,
    @SerializedName("3")OVERWRITE,
    @SerializedName("4")ASK_ME,
    @SerializedName("5")FROM_PARENT
}
