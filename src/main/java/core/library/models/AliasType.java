package core.library.models;

import com.google.gson.annotations.SerializedName;

public enum AliasType {

    @SerializedName("SCRIPT") SCRIPT("aliasType.SCRIPT"),
    @SerializedName("BINARY_FILE") BINARY_FILE("aliasType.BINARY_FILE");

    private String nameLangKey;

    AliasType(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }

    public String getNameLangKey() {
        return nameLangKey;
    }

}
