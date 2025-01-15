package edu.store.vaadin.ui.common.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import org.vaadin.firitin.components.button.VButton;

public class DialogTemplate extends Dialog {

    private final Div divContent = new Div();

    public DialogTemplate(String title) {
        setSizeFull();

        getElement().setAttribute("aria-label", "Add note");
        setSizeFull();
        setDraggable(true);
        setResizable(true);
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        setModal(true);
        setHeaderTitle(title);
        VButton closeButton = new VButton(new Icon("lumo", "cross"), e -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getHeader().add(closeButton);

        divContent.setSizeFull();
        add(divContent);
    }

    public void addContent(Component... components) {
        divContent.add(components);
    }
}
