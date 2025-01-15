/* (C)2021 www.neumann-itcs.com */
package edu.store.vaadin.ui.translation;

import com.vaadin.flow.i18n.I18NProvider;
import java.io.Serial;
import java.text.MessageFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyTranslationProvider implements I18NProvider {

    public static final String BUNDLE_PREFIX = "translate";

    /** */
    @Serial
    private static final long serialVersionUID = 341460518074997524L;

    public final Locale LOCALE_DE = new Locale("de", "DE");
    public final Locale LOCALE_EN = new Locale("en", "GB");

    public final Locale LOCALE_PL = new Locale("pl", "PL");
    private final transient Logger logger = LoggerFactory.getLogger(MyTranslationProvider.class);

    private final List<Locale> locales = List.of(LOCALE_DE, LOCALE_EN, LOCALE_PL);

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(MyTranslationProvider.class.getName()).warn("null key would not find any value");
            return "";
        }

        logger.info("Get translation for key:{} and language: {}", key, locale.getCountry());

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, new Locale("pl"));

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            logger.info("MissingResourceException...");
            // try to load data from TranslationTable. The values don't have parameters
            // thats
            // because we can return the value immediatelly

            LoggerFactory.getLogger(MyTranslationProvider.class.getName()).warn(
                "Missing resource, try to load translations from Translation table...",
                e
            );
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
}
