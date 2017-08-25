package core.library.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alias {

    @Expose @SerializedName("key")  private String key;
    @Expose @SerializedName("aliasType")  private AliasType aliasType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AliasType getAliasType() {
        return aliasType;
    }

    public void setAliasType(AliasType aliasType) {
        this.aliasType = aliasType;
    }

}
