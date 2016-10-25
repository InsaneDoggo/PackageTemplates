package global;

import global.models.File;

/**
 * Created by Arsen on 04.09.2016.
 */
public interface Const {

    boolean IS_DEBUG = false;

    String EXPORT_FILE_NAME = "Templates.json";
    String MODELS_PACKAGE_PATH = File.class.getCanonicalName().substring(0, File.class.getCanonicalName().length() - File.class.getSimpleName().length());
    String ACTION_PREFIX = "PT_.Action_";

    String NODE_GROUP_DEFAULT = "default";

    String PACKAGE_TEMPLATES_DIR_NAME = "packageTemplates";
    String PACKAGE_TEMPLATES_EXTENSION = "json";

    int MESSAGE_MAX_LENGTH = 50;
}
