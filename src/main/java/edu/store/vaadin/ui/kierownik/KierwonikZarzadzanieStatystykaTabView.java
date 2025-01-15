package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VDiv;

public class KierwonikZarzadzanieStatystykaTabView extends VDiv {

    public KierwonikZarzadzanieStatystykaTabView() {
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton button = new VButton("dodaj Statystyke", VaadinIcon.FILE_ADD.create());
        add(button);
    }
}
