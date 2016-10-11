package global;

import global.models.File;

/**
 * Created by Arsen on 04.09.2016.
 */
public interface Const {

    boolean IS_DEBUG = false;

    String EXPORT_FILE_NAME = "Templates.json";
    String MODELS_PACKAGE_PATH = File.class.getCanonicalName().substring(0, File.class.getCanonicalName().length() - File.class.getSimpleName().length());
    String ACTION_PREFIX = "PT_Action_";

    String NODE_GROUP_DEFAULT = "default";
}
