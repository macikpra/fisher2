package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import edu.store.database.entities.CfgRole;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;

public class KierwonikZarzadzanieTowarTabView extends VDiv {
    private final VTextField nazwa = new VTextField("Nazwa");
    private final VButton tmp = new VButton("Towar");
    private final VComboBox<CfgRole> rodzajCbx = new VComboBox<>("Rodzaj");
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
    public KierwonikZarzadzanieTowarTabView() {
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton addTowar = new VButton(VaadinIcon.PLUS.create(), l -> openTowarEditor());
        VButton refresh = new VButton(VaadinIcon.REFRESH.create(), l -> selectAllFromDb());
        add(
                new VHorizontalLayout( nazwa,rodzajCbx, buttonSearch, refresh, addTowar)
                        .withSpacing(true)
                        .withPadding(false)
                        .withMargin(false)
                        .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        add(tmp);
        initGrid();
    }
    private void performSearchInGrid() {
        if(isNazwaEntered()){

        }
    }
    private boolean isNazwaEntered() {
        return !StringUtils.isEmpty(nazwa.getValue()) && nazwa.getValue().length() >= 3;
    }
    private void initGrid() {

    }
    private void selectAllFromDb() {

    }
    private void openTowarEditor() {

    }
}