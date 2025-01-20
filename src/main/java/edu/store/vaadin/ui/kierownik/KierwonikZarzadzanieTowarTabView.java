package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import edu.store.database.entities.CfgRole;
import edu.store.database.repositories.TowarRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.dto.TowarDto;
import edu.store.model.mapper.TowarMapper;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;

public class KierwonikZarzadzanieTowarTabView extends VDiv {
    private final VGrid<TowarDto> grid = new VGrid<>(TowarDto.class);
    private final VTextField nazwa = new VTextField("Nazwa");
    private final TowarRepository towarRepository;
    private final TowarMapper towarMapper;
    private final VComboBox<CfgRole> rodzajCbx = new VComboBox<>("Rodzaj");
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
    SklepDto sklepDto;
    public KierwonikZarzadzanieTowarTabView(
            TowarRepository towarRepository,
            TowarMapper towarMapper
    ) {
        this.towarRepository = towarRepository;
        this.towarMapper = towarMapper;
        this.sklepDto = UI.getCurrent().getSession().getAttribute(SklepDto.class);
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton addTowar = new VButton(VaadinIcon.PLUS.create(), l -> openTowarEditor(new TowarDto()));
        VButton refresh = new VButton(VaadinIcon.REFRESH.create(), l -> selectAllFromDb());
        add(
                new VHorizontalLayout( nazwa,rodzajCbx, buttonSearch, refresh, addTowar)
                        .withSpacing(true)
                        .withPadding(false)
                        .withMargin(false)
                        .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        initGrid();
        selectAllFromDb();
    }
    private void performSearchInGrid() {
        if(isNazwaEntered()){

        }
    }
    private boolean isNazwaEntered() {
        return !StringUtils.isEmpty(nazwa.getValue()) && nazwa.getValue().length() >= 3;
    }
    private void initGrid() {
        grid.setWidthFull();
        grid.addColumn(
            new ComponentRenderer<>(sprzet -> new VButton("Zamow", VaadinIcon.EDIT.create(), clickEvent -> zamow(sprzet)))
        )
        .setHeader("Zamow")
        .setAutoWidth(true);
        grid.addColumn(
            new ComponentRenderer<>(towar -> {
                VButton editButton = new VButton("Edytuj", VaadinIcon.EDIT.create(), clickEvent -> openTowarEditor(towar));
                VButton deleteButton = new VButton("Usun", VaadinIcon.TRASH.create(), clickEvent -> deleteTowar(towar));
                deleteButton.getStyle().set("color", "red");
                return new VHorizontalLayout(editButton, deleteButton);
            })
        )
        .setHeader("Akcje")
        .setAutoWidth(true);
        //grid.removeColumnByKey("sklepId");
        add(grid);
    }
    private void selectAllFromDb() {
        grid.setItems(towarRepository.findBySklepId(sklepDto.getId()).stream().map(towarMapper::toDto).toList());
        nazwa.setValue("");
    }
    private void openTowarEditor(TowarDto towarDto) {

    }
    private void deleteTowar(TowarDto towarDto) {

    }
    private void zamow(TowarDto sprzet) {

    }
}