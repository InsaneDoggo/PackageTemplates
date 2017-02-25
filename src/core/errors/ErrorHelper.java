package core.errors;

/**
 * Created by Arsen on 25.02.2017.
 */
public class ErrorHelper {

    private String error;

    private ErrorHelper() {
        error = null;
    }


    //=================================================================
    //  API
    //=================================================================
    public static void add(String errorText) {
        getInstance().error = errorText;
    }

    public static String get() {
        return getInstance().error;
    }


    //=================================================================
    //  Singleton
    //=================================================================
    private static class LazyHolder {
        private static final ErrorHelper INSTANCE = new ErrorHelper();
    }

    private static ErrorHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

}
