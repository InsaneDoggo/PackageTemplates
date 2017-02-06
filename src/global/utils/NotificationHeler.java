package global.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Created by Arsen on 06.02.2017.
 */
public class NotificationHeler {

    public static void error(String title, String message){
        Notifications.Bus.notify(new Notification("PackageTemplates Plugin", title, message, NotificationType.ERROR));
    }

    public static void info(String title, String message){
        Notifications.Bus.notify(new Notification("PackageTemplates Plugin", title, message, NotificationType.INFORMATION));
    }

}
