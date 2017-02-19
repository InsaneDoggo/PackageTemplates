package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by Arsen on 29.09.2016.
 */
public interface PluginIcons {

    Icon CUSTOM_PATH = IconLoader.getIcon("/icons/custom_path.png");
    Icon CUSTOM_PATH_DISABLED = IconLoader.getIcon("/icons/custom_path_disabled.png");

    Icon WRITE_RULES_DISABLED = IconLoader.getIcon("/icons/write_rules_disabled.png");
    Icon WRITE_RULES_HARD = IconLoader.getIcon("/icons/write_rules_hard.png");
    Icon WRITE_RULES_NEUTRAL = IconLoader.getIcon("/icons/write_rules_neutral.png");
    Icon WRITE_RULES_SOFT = IconLoader.getIcon("/icons/write_rules_soft.png");


    Icon SCRIPT = IconLoader.getIcon("/icons/script.png");
    Icon SCRIPT_DISABLED = IconLoader.getIcon("/icons/script_disabled.png");

    Icon PACKAGE_TEMPLATES = IconLoader.getIcon("/icons/package_templates.png");

}
