package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import edu.store.database.entities.CfgRole;
import edu.store.database.entities.Sprzet;
import edu.store.database.entities.TypSprzetu;
import edu.store.database.repositories.SprzetRepository;
import edu.store.database.repositories.TypSprzetuRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.dto.SprzetDto;
import edu.store.model.dto.TowarDto;
import edu.store.model.mapper.SprzetMapper;
import edu.store.vaadin.ui.common.dialog.DialogTemplate;
import edu.store.vaadin.ui.common.dialog.ErrorMessageDialog;
import edu.store.vaadin.ui.editors.sprzet.SprzetEditor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class KierwonikZarzadzanieSprzetTabView extends VDiv {
    private final VGrid<SprzetDto> grid = new VGrid<>(SprzetDto.class);
    private final VGrid<SprzetDto> orderGrid = new VGrid<>(SprzetDto.class);
    private final Map<SprzetDto, Integer> quantities = new HashMap<>();
    private final VTextField nazwa = new VTextField("Nazwa");
    private final SprzetRepository sprzetRepository;
    private final SprzetMapper sprzetMapper;
    private final TypSprzetuRepository typSprzetuRepository;
    private final VComboBox<TypSprzetu> rodzaj = new VComboBox<>("Rodzaj");
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
    VerticalLayout firstGridLayout = new VerticalLayout();
    VerticalLayout secondGridLayout = new VerticalLayout();
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
        firstGridLayout.add(
            new VHorizontalLayout( nazwa,rodzaj, buttonSearch, refresh, addSprzet)
                .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        VButton orderSprzet = new VButton("Zamow",l->order());
        orderSprzet.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        VButton rejectSprzet = new VButton("Odrzuc",l->reject());
        secondGridLayout.add(
                new VHorizontalLayout(orderSprzet,rejectSprzet)
                        .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        HorizontalLayout mainLayout = new HorizontalLayout(firstGridLayout,secondGridLayout);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, firstGridLayout, secondGridLayout);
        add(mainLayout);
        initFirstGrid();
        initSecondGrid();
        selectAllFromDb();
    }

    private void order() {
        for (Map.Entry<SprzetDto, Integer> entry : quantities.entrySet()) {
            SprzetDto sprzet = entry.getKey();
            Integer ilosc = entry.getValue();
            for(int i = 0; i < ilosc; i++){
                SprzetDto clone = new SprzetDto();
                clone.setNazwa(sprzet.getNazwa());
                clone.setSklepId(sprzet.getSklepId());
                clone.setCena(sprzet.getCena());
                clone.setTypSprzetu(sprzet.getTypSprzetu());
                log.info("entrySaved for {}....{}", SprzetDto.class.getSimpleName(), clone);
                SprzetDto managed = sprzetMapper.toDto(sprzetRepository.save(sprzetMapper.toEntity(clone)));
                log.info("Sklep saved: {}", managed);
            }
        }
        selectAllFromDb();
        quantities.clear();
        orderGrid.getDataProvider().refreshAll();
    }
    private void orderItem(SprzetDto sprzetDto) {
        if (!quantities.containsKey(sprzetDto)) {
            quantities.put(sprzetDto, 1); // Default quantity is 1
            orderGrid.setItems(quantities.keySet()); // Refresh the grid's items
        } else {
            Notification.show("Sprzet jest juz na liscie", 2000, Notification.Position.MIDDLE);
        }
    }
    private void reject(){
        quantities.clear();
        orderGrid.getDataProvider().refreshAll();
    }
    private void rejectItem(SprzetDto sprzetDto) {
        if (quantities.containsKey(sprzetDto)) {
            quantities.remove(sprzetDto);
            orderGrid.setItems(quantities.keySet());
        } else {
            Notification.show("Tego sprzetu nie ma na liscie", 2000, Notification.Position.MIDDLE);
        }
    }
    private void incItem(SprzetDto sprzetDto) {
        quantities.put(sprzetDto, quantities.getOrDefault(sprzetDto, 1) + 1);
        orderGrid.getDataProvider().refreshAll();
    }
    private void decItem(SprzetDto sprzetDto) {
        int currentQuantity = quantities.getOrDefault(sprzetDto, 1);
        if (currentQuantity > 1) {
            quantities.put(sprzetDto, currentQuantity - 1);
            orderGrid.getDataProvider().refreshAll();
        } else {
            Notification.show("Nie moze byc mniejsze niz 1", 2000, Notification.Position.MIDDLE);
        }
    }
    private void initSecondGrid() {
        orderGrid.setWidthFull();
        orderGrid.addColumn(new ComponentRenderer<>(item -> {
                VTextField textField = new VTextField();
                textField.setValue(String.valueOf(quantities.getOrDefault(item, 1)));
                textField.setWidthFull();
                textField.addValueChangeListener(event -> {
                    try {
                        Integer newQuantity = Integer.parseInt(event.getValue());
                        quantities.put(item, newQuantity);
                    } catch (NumberFormatException e) {
                        Notification.show("Prosze podac poprawna ilosc", 2000, Notification.Position.MIDDLE);
                    }
                });
                return textField;
            })
        )
        .setHeader("Ilosc")
        .setAutoWidth(true);
        orderGrid.addColumn(
            new ComponentRenderer<>(sprzet -> {
                VButton editButton = new VButton( VaadinIcon.ARROW_LEFT.create(), clickEvent -> rejectItem(sprzet));
                VButton addButton = new VButton( VaadinIcon.PLUS.create(), clickEvent -> incItem(sprzet));
                VButton removeButton = new VButton( VaadinIcon.MINUS.create(), clickEvent -> decItem(sprzet));
                return new VHorizontalLayout(editButton,addButton, removeButton);
            })
        )
        .setHeader("Akcje")
        .setAutoWidth(true);
        orderGrid.removeColumnByKey("sklepId");
        orderGrid.removeColumnByKey("typSprzetu");
        orderGrid.removeColumnByKey("cena");
        orderGrid.setItems(quantities.keySet());
        secondGridLayout.add(orderGrid);
    }
    private void performSearchInGrid() {
        if(isNazwaEntered() && isRodzajChosen()){
            log.info("in search for both name and type");
            grid.setItems(
                sprzetRepository
                .findByNazwaContainingIgnoreCaseAndTypSprzetuAndSklepId(nazwa.getValue(),rodzaj.getValue().getId(),sklepDto.getId())
                .stream()
                .map(sprzetMapper::toDto)
                .toList()
            );
        }else if(isNazwaEntered()){
            log.info("in search for name");
            grid.setItems(
                sprzetRepository
                .findByNazwaContainingIgnoreCaseAndSklepId(nazwa.getValue(),sklepDto.getId())
                .stream()
                .map(sprzetMapper::toDto)
                .toList()
            );
        }else if(isRodzajChosen()) {
            log.info("in search for typ");
            grid.setItems(
                sprzetRepository
                .findByTypSprzetuAndSklepId(rodzaj.getValue().getId(),sklepDto.getId())
                .stream()
                .map(sprzetMapper::toDto)
                .toList()
            );
        }
    }
    private boolean isNazwaEntered() {
        return !StringUtils.isEmpty(nazwa.getValue()) && nazwa.getValue().length() >= 3;
    }
    private boolean isRodzajChosen() {
        return rodzaj.getValue() != null && rodzaj.getValue().getNazwa().equals("Wszystko");
    }
    private void initFirstGrid() {
        grid.setWidthFull();
        grid.addColumn(
            new ComponentRenderer<>(sprzet -> new VButton(VaadinIcon.ARROW_RIGHT.create(), clickEvent -> orderItem(sprzet)))
        )
        .setHeader("Zamow")
        .setAutoWidth(true);
        grid.addColumn(
            new ComponentRenderer<>(sprzet -> {
                VButton editButton = new VButton( VaadinIcon.EDIT.create(), clickEvent -> openSprzetEditor(sprzet));
                VButton deleteButton = new VButton( VaadinIcon.TRASH.create(), clickEvent -> deleteSprzet(sprzet));
                deleteButton.getStyle().set("color", "red");
                return new VHorizontalLayout(editButton, deleteButton);
            })
        )
        .setHeader("Akcje")
        .setAutoWidth(true);
        grid.removeColumnByKey("sklepId");
        firstGridLayout.add(grid);
    }

    private void selectAllFromDb() {
        grid.setItems(sprzetRepository.findBySklepId(sklepDto.getId()).stream().map(sprzetMapper::toDto).toList());
        nazwa.setValue("");
    }
    private void openSprzetEditor(SprzetDto sprzetDto) {
        DialogTemplate dlg = new DialogTemplate(
                sprzetDto.getId() == null ? "Dodaj nowy sprzet" : "Modyfikuj dane sprzetu"
        );
        SprzetEditor editor = new SprzetEditor();
        editor.setSavedHandler(
            (AbstractForm.SavedHandler<SprzetDto>) sprzetDtoHandler -> {
                BinderValidationStatus<SprzetDto> status = editor.getBinder().validate();
                if (!status.isOk()) {
                    Notification.show(
                        "Proszę sprawdzić dane - nie wszystkie pola są wypełnione poprawnie!",
                        3000,
                        Notification.Position.MIDDLE
                    );
                }  else {
                    try {
                        entrySaved(sprzetDtoHandler);
                        dlg.close();
                    } catch (Exception ex) {
                        log.error("Error: " + ex.getMessage(), ex);
                        ErrorMessageDialog emd = new ErrorMessageDialog(
                            ex,
                            "Prosimy o zapoznanie się z listą błędów i przesłanie jej do administratora"
                        );
                        emd.open();
                    }
                }
            }
        );
        editor.setResetHandler(
            (AbstractForm.ResetHandler<SprzetDto>) investorDto ->
                log.info("user have closed window without saving their data....")
        );
        editor.setEntity(sprzetDto);
        editor.getResetButton().setEnabled(true);
        editor.getResetButton()
                .addClickListener(l -> dlg.close());
        editor.getSaveButton().setEnabled(true);
        editor.getDeleteButton().setVisible(false);
        dlg.addContent(editor);
        dlg.open();
    }
    private void entrySaved(SprzetDto clone) {
        log.info("entrySaved for {}....{}", SprzetDto.class.getSimpleName(), clone);
        Sprzet sprzetEntity = new Sprzet();
        SprzetDto managed = sprzetMapper.toDto(sprzetRepository.save(sprzetMapper.toEntity(clone)));
        log.info("Sklep saved: {}", managed);
        selectAllFromDb();
    }
    private void deleteSprzet(SprzetDto sprzetDto) {
        log.info("Deleting Sklep: {}", sprzetDto);
        sprzetRepository.deleteById(sprzetDto.getId());
        selectAllFromDb();
        log.info("Sklep with ID {} deleted", sprzetDto.getId());
    }
}
