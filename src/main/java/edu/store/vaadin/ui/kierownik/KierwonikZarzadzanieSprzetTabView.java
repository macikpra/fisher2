package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import edu.store.database.entities.CfgRole;
import edu.store.database.entities.TypSprzetu;
import edu.store.database.repositories.SprzetRepository;
import edu.store.database.repositories.TypSprzetuRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.dto.SprzetDto;
import edu.store.model.mapper.SprzetMapper;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;

import java.util.List;

public class KierwonikZarzadzanieSprzetTabView extends VDiv {
    private final VGrid<SprzetDto> grid = new VGrid<>(SprzetDto.class);
    private final VTextField nazwa = new VTextField("Nazwa");
    private final SprzetRepository sprzetRepository;
    private final SprzetMapper sprzetMapper;
    private final TypSprzetuRepository typSprzetuRepository;
    private final VComboBox<TypSprzetu> rodzaj = new VComboBox<>("Rodzaj");
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
    SklepDto sklepDto;
    public KierwonikZarzadzanieSprzetTabView(
            SprzetRepository sprzetRepository,
            SprzetMapper sprzetMapper,
            TypSprzetuRepository typSprzetuRepository
    ) {
        this.sprzetRepository = sprzetRepository;
        this.sprzetMapper = sprzetMapper;
        this.typSprzetuRepository = typSprzetuRepository;
        this.sklepDto = UI.getCurrent().getSession().getAttribute(SklepDto.class);
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton addSprzet = new VButton(VaadinIcon.PLUS.create(), l -> openSprzetEditor(new SprzetDto()));
        VButton refresh = new VButton(VaadinIcon.REFRESH.create(), l -> selectAllFromDb());
        List<TypSprzetu> typSprzetuList = typSprzetuRepository.findAll();
        TypSprzetu typSprzetu = new TypSprzetu("Wszystko",0L);
        typSprzetuList.add(0,typSprzetu);
        rodzaj.setItemLabelGenerator(TypSprzetu::getNazwa);
        rodzaj.setItems(typSprzetuList);
        rodzaj.setValue(typSprzetu);
        add(
            new VHorizontalLayout( nazwa,rodzaj, buttonSearch, refresh, addSprzet)
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
            new ComponentRenderer<>(sprzet -> {
                VButton editButton = new VButton("Edytuj", VaadinIcon.EDIT.create(), clickEvent -> openSprzetEditor(sprzet));
                VButton deleteButton = new VButton("Usun", VaadinIcon.TRASH.create(), clickEvent -> deleteSprzet(sprzet));
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
        grid.setItems(sprzetRepository.findBySklepId(sklepDto.getId()).stream().map(sprzetMapper::toDto).toList());
        nazwa.setValue("");
    }
    private void openSprzetEditor(SprzetDto sprzetDto) {

    }
    private void deleteSprzet(SprzetDto sprzetDto) {
    }
    private void zamow(SprzetDto sprzet) {

    }
}
