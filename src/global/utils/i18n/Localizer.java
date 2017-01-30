package global.utils.i18n;


import core.state.util.SaveUtil;
import global.Const;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by CeH9 on 23.07.2016.
 */
public class Localizer {

    private static ResourceBundle bundle;

    private static Language currentLang;

    private static Language getCurrentLang() {
        if (currentLang == null) {
            currentLang = SaveUtil.reader().getLanguage();
        }
        return currentLang;
    }

    public static ResourceBundle getBundle(Language lang) {
        if (bundle == null || getCurrentLang() != lang) {
            switch (lang) {
                case RU:
                    bundle = ResourceBundle.getBundle("/localization/MyResources_ru_RU", new Locale("ru_RU"));
                    currentLang = Language.RU;
                    break;
                case EN:
                default:
                    bundle = ResourceBundle.getBundle("/localization/MyResources_en_US", new Locale("en_US"));
                    currentLang = Language.EN;
                    break;
            }
        }

        return bundle;
    }

    public static String get(String key) {
        return get(key, getCurrentLang());
    }

    public static String get(String key, Language lang) {
        if (lang == Language.RU) {
            return new String(getBundle(Language.RU).getString(key).getBytes(Const.charsets.ISO_8859_1), Const.charsets.UTF_8);
        } else {
            return getBundle(lang).getString(key);
        }
    }

}
