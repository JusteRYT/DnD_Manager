package com.example.dnd_manager.lang;


import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public final class I18n {

    private static Locale currentLocale = Locale.ENGLISH;
    private static ResourceBundle bundle = loadBundle(currentLocale);

    private I18n() {}

    private static ResourceBundle loadBundle(Locale locale) {
        try {
            return new PropertyResourceBundle(new InputStreamReader(
                    Objects.requireNonNull(I18n.class.getClassLoader().getResourceAsStream("lang/messages_" + locale.getLanguage() + ".properties")),
                    StandardCharsets.UTF_8
            ));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load localization file for " + locale, e);
        }
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = loadBundle(locale);
    }

    public static Locale getLocale() {
        return currentLocale;
    }

    public static String t(String key) {
        return bundle.getString(key);
    }

    public static boolean isEnglish() {
        return currentLocale.equals(Locale.ENGLISH);
    }
}
