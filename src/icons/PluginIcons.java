package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by Arsen on 29.09.2016.
 */
public interface PluginIcons {

    Icon CUSTOM_PATH = IconLoader.getIcon("/icons/custom_path.png");
    Icon CUSTOM_PATH_DISABLED = IconLoader.getIcon("/icons/custom_path_disabled.png");

    Icon SCRIPT = IconLoader.getIcon("/icons/script.png");
    Icon SCRIPT_DISABLED = IconLoader.getIcon("/icons/script_disabled.png");

    Icon PACKAGE_TEMPLATES = IconLoader.getIcon("/icons/package_templates.png");

}
