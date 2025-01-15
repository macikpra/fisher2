package edu.store.vaadin.ui.common.dialog;

import com.flowingcode.vaadin.addons.errorwindow.ErrorWindow;
import com.flowingcode.vaadin.addons.errorwindow.ErrorWindowI18n;

public class ErrorMessageDialog extends ErrorWindow {

    private static ErrorWindowI18n i18n = ErrorWindowI18n.createDefault();

    static {
        i18n.setCaption("Wystąpił błąd");
        i18n.setInstructions("Prosze przeslij błąd do Admina");
        i18n.setClose("Ok");
        i18n.setDetails("Rozwin loga");
        i18n.setClipboard("Skopiuj błąd do schowka");
        i18n.setDefaultErrorMessage("Prosze przeslij błąd do Admina");
    }

    public ErrorMessageDialog(Throwable cause, String errorMessage) {
        super(cause, errorMessage, i18n);
    }
}
