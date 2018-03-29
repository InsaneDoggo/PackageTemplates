package global.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arsen on 06.02.2017.
 */
public class NotificationHelper {

    public static void error(String title, String message) {
        Notifications.Bus.notify(new Notification("PackageTemplates Plugin", title, message + " " + getTimePrefix(), NotificationType.ERROR));
    }

    public static void info(String title, String message) {
        Notifications.Bus.notify(new Notification("PackageTemplates Plugin", title, message + " " + getTimePrefix(), NotificationType.INFORMATION));
    }

    private static String getTimePrefix() {
        DateFormat dateFormat = new SimpleDateFormat("(HH:mm:ss)");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
