package global.utils;

import global.Const;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class Logger {

    public static void log(String text) {
        if (Const.IS_DEBUG) {
            System.out.println(text);

            if (Const.SHOULD_LOG_TO_NOTIFICATION) {
                NotificationHelper.info("PackageTemplate LOG ", text);
            }
        }
    }

    public static void printStack(Throwable e) {
        if (Const.IS_DEBUG) {
            e.printStackTrace(System.out);

            if (Const.SHOULD_LOG_TO_NOTIFICATION) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);

                NotificationHelper.error("PackageTemplate TRACE", sw.toString());
            }
        }
    }

    public static void logAndPrintStack(String text, Throwable e) {
        log(text);
        printStack(e);
    }


}
