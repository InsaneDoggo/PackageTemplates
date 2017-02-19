package core.writeRules;

import com.google.gson.annotations.SerializedName;
import icons.PluginIcons;

import javax.swing.*;

/**
 * Created by Arsen on 11.09.2016.
 */
public enum WriteRules {
    @SerializedName("FROM_PARENT")FROM_PARENT("WriteRules.FromParent"),
    @SerializedName("ASK_ME")ASK_ME("WriteRules.AskMe"),
    @SerializedName("OVERWRITE")OVERWRITE("WriteRules.Overwrite"),
    @SerializedName("USE_EXISTING")USE_EXISTING("WriteRules.UseExisting");

    private String nameLangKey;

    WriteRules(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }

    public String getNameLangKey() {
        return nameLangKey;
    }

    public Icon toIcon() {
        switch (this) {
            case FROM_PARENT:
                return PluginIcons.WRITE_RULES_DISABLED;
            case ASK_ME:
                return PluginIcons.WRITE_RULES_NEUTRAL;
            case OVERWRITE:
                return PluginIcons.WRITE_RULES_HARD;
            case USE_EXISTING:
                return PluginIcons.WRITE_RULES_SOFT;
        }

        throw new RuntimeException("Icon for " + name() + " not found!");
    }

    public static WriteRules getDefault() {
        return FROM_PARENT;
    }

}
