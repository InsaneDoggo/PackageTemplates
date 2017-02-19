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
            NotificationHelper.info("PackageTemplate LOG ", text);
        }
    }

    public static void printStack(Throwable e) {
        if (Const.IS_DEBUG) {
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            NotificationHelper.error("PackageTemplate TRACE", sw.toString());
        }
    }

    public static void logAndPrintStack(String text, Throwable e) {
        if (Const.IS_DEBUG) {
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            NotificationHelper.error("PackageTemplate ", text + "\n" + sw.toString());
        }
    }


}
