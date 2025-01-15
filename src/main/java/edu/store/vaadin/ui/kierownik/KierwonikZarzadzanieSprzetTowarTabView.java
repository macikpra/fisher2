package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VDiv;

public class KierwonikZarzadzanieSprzetTowarTabView extends VDiv {

    public KierwonikZarzadzanieSprzetTowarTabView() {
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton button = new VButton("dodaj Sprzet/Towar", VaadinIcon.FILE_ADD.create());
        add(button);
    }
}
