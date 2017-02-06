package global.utils;

import global.Const;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class Logger {

    public static void log(String text) {
        if (Const.IS_DEBUG) System.out.println(text);
    }

    public static void printStack(Exception e) {
        if (Const.IS_DEBUG) e.printStackTrace();
    }

}
