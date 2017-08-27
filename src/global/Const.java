package global;

import global.models.File;

import java.nio.charset.Charset;

/**
 * Created by Arsen on 04.09.2016.
 */
public interface Const {

    boolean IS_DEBUG = true;
    boolean SHOULD_LOG_TO_NOTIFICATION = false;

    String ACTION_PREFIX = "pt.action.";

    String MODELS_PACKAGE_PATH = File.class.getCanonicalName().substring(
            0, File.class.getCanonicalName().length() - File.class.getSimpleName().length());

    String LIBRARIES_DIR_NAME = "packageTemplatesLibraries";
    String BINARY_FILES_DIR_NAME = "packageTemplatesBinaryFiles";
    String CACHE_DIR_NAME = "PackageTemplatesCache";
    String BINARY_FILES_CACHE_DIR_NAME = "BinaryFiles";
    String PACKAGE_TEMPLATES_DIR_NAME = "packageTemplates";
    String PACKAGE_TEMPLATES_EXTENSION = "json";
    String FILE_EXTENSION_SEPARATOR = ".";
    String ENCODED_NAME_EXT_DELIMITER = "\u0F0Fext\u0F0F.";

    int MESSAGE_MAX_LENGTH = 50;
    int FAVOURITE_NAME_LIMIT = 70;

    String DIR_USER = "";
    String DIR_INTERNAL = "internal";
    String DIR_J2EE = "j2ee";

    String TUTORIALS_URL = "http://ceh9.github.io/PackageTemplates/";


    interface charsets {
        Charset UTF_8 = Charset.forName("UTF-8");
        Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    }

    interface Key {
        String CTX_FULL_PATH = "CTX_FULL_PATH";
        String CTX_DIR_PATH = "CTX_DIR_PATH";
    }

}
