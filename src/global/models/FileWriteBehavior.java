package global.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 11.09.2016.
 */
public enum FileWriteBehavior {
    @SerializedName("1")CREATE_IF_NOT_EXIST,
    @SerializedName("1")OVERWRITE,
    @SerializedName("1")ASK_ME,
    @SerializedName("1")FROM_PARENT
}
