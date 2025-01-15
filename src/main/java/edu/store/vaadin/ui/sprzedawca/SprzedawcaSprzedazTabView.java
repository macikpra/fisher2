package edu.store.vaadin.ui.sprzedawca;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VDiv;

public class SprzedawcaSprzedazTabView extends VDiv {

    public SprzedawcaSprzedazTabView() {
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton button = new VButton("dodaj pracownika", VaadinIcon.FILE_ADD.create());
        add(button);
    }
}
