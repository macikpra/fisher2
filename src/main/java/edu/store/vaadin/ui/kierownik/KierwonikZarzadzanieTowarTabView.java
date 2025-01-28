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
import edu.store.database.entities.TypTowaru;
import edu.store.database.repositories.TowarRepository;
import edu.store.database.repositories.TypTowaruRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.dto.SprzetDto;
import edu.store.model.dto.TowarDto;
import edu.store.model.mapper.TowarMapper;
import edu.store.vaadin.ui.common.dialog.DialogTemplate;
import edu.store.vaadin.ui.common.dialog.ErrorMessageDialog;
import edu.store.vaadin.ui.editors.towar.TowarEditor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class KierwonikZarzadzanieTowarTabView extends VDiv {
    private final VGrid<TowarDto> grid = new VGrid<>(TowarDto.class);
    private final VGrid<TowarDto> orderGrid = new VGrid<>(TowarDto.class);
    private final VTextField nazwa = new VTextField("Nazwa");
    private final Map<TowarDto, Integer> quantities = new HashMap<>();
    private final TowarRepository towarRepository;
    private final TowarMapper towarMapper;
    private final TypTowaruRepository typTowaruRepository;
    private final VComboBox<TypTowaru> rodzaj = new VComboBox<>("Rodzaj");
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
    VerticalLayout firstGridLayout = new VerticalLayout();
    VerticalLayout secondGridLayout = new VerticalLayout();
    SklepDto sklepDto;
    public KierwonikZarzadzanieTowarTabView(
            TowarRepository towarRepository,
            TowarMapper towarMapper,
            TypTowaruRepository typTowaruRepository
    ) {
        this.towarRepository = towarRepository;
        this.towarMapper = towarMapper;
        this.typTowaruRepository = typTowaruRepository;
        this.sklepDto = UI.getCurrent().getSession().getAttribute(SklepDto.class);
        setSizeFull();
        getElement().getStyle().set("overflow", "auto");
        VButton addTowar = new VButton(VaadinIcon.PLUS.create(), l -> openTowarEditor(new TowarDto()));
        VButton refresh = new VButton(VaadinIcon.REFRESH.create(), l -> selectAllFromDb());
        List<TypTowaru> typTowaruList = typTowaruRepository.findAll();
        TypTowaru typTowaru = new TypTowaru("Wszystko",0L);
        typTowaruList.add(0,typTowaru);
        rodzaj.setItemLabelGenerator(TypTowaru::getNazwa);
        rodzaj.setItems(typTowaruList);
        rodzaj.setValue(typTowaru);
        firstGridLayout.add(
            new VHorizontalLayout( nazwa,rodzaj, buttonSearch, refresh, addTowar)
                .withSpacing(true)
                .withPadding(false)
                .withMargin(false)
                .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        VButton orderTowar = new VButton("Zamow",l->order());
        orderTowar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        VButton rejectTowar = new VButton("Odrzuc",l->reject());
        secondGridLayout.add(
                new VHorizontalLayout(orderTowar,rejectTowar)
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
        for (Map.Entry<TowarDto, Integer> entry : quantities.entrySet()) {
            TowarDto towar = entry.getKey();
            Integer ilosc = entry.getValue();
            if(ilosc >0){
                towar.setIlosc(towar.getIlosc()+Long.valueOf(ilosc.longValue()));
                log.info("entrySaved for {}....{}", TowarDto.class.getSimpleName(), towar);
                TowarDto managed = towarMapper.toDto(towarRepository.save(towarMapper.toEntity(towar)));
                log.info("Sklep saved: {}", managed);
            }
        }
        selectAllFromDb();
        quantities.clear();
        orderGrid.getDataProvider().refreshAll();
    }
    private void orderItem(TowarDto towarDto) {
        if (!quantities.containsKey(towarDto)) {
            quantities.put(towarDto, 1); // Default quantity is 1
            orderGrid.setItems(quantities.keySet()); // Refresh the grid's items
        } else {
            Notification.show("Sprzet jest juz na liscie", 2000, Notification.Position.MIDDLE);
        }
    }
    private void reject(){
        quantities.clear();
        orderGrid.getDataProvider().refreshAll();
    }
    private void rejectItem(TowarDto towarDto) {
        if (quantities.containsKey(towarDto)) {
            quantities.remove(towarDto);
            orderGrid.setItems(quantities.keySet());
        } else {
            Notification.show("Tego sprzetu nie ma na liscie", 2000, Notification.Position.MIDDLE);
        }
    }
    private void incItem(TowarDto towarDto) {
        quantities.put(towarDto, quantities.getOrDefault(towarDto, 1) + 1);
        orderGrid.getDataProvider().refreshAll();
    }
    private void decItem(TowarDto towarDto) {
        int currentQuantity = quantities.getOrDefault(towarDto, 1);
        if (currentQuantity > 1) {
            quantities.put(towarDto, currentQuantity - 1);
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
            new ComponentRenderer<>(towar -> {
                VButton editButton = new VButton( VaadinIcon.ARROW_LEFT.create(), clickEvent -> rejectItem(towar));
                VButton addButton = new VButton( VaadinIcon.PLUS.create(), clickEvent -> incItem(towar));
                VButton removeButton = new VButton( VaadinIcon.MINUS.create(), clickEvent -> decItem(towar));
                return new VHorizontalLayout(editButton,addButton, removeButton);
            })
        )
        .setHeader("Akcje")
        .setAutoWidth(true);
        orderGrid.removeColumnByKey("sklepId");
        orderGrid.removeColumnByKey("typTowaru");
        orderGrid.removeColumnByKey("ilosc");
        orderGrid.removeColumnByKey("cena");
        orderGrid.setItems(quantities.keySet());
        secondGridLayout.add(orderGrid);
    }
    private void performSearchInGrid() {
        if(isNazwaEntered() && isRodzajChosen()){
            log.info("in search for both name and type");
            grid.setItems(
                towarRepository
                .findByNazwaContainingIgnoreCaseAndTypTowaruAndSklepId(nazwa.getValue(),rodzaj.getValue().getId(),sklepDto.getId())
                .stream()
                .map(towarMapper::toDto)
                .toList()
            );
        }else if(isNazwaEntered()){
            log.info("in search for name");
            grid.setItems(
                towarRepository
                .findByNazwaContainingIgnoreCaseAndSklepId(nazwa.getValue(),sklepDto.getId())
                .stream()
                .map(towarMapper::toDto)
                .toList()
            );
        }else if(isRodzajChosen()) {
            log.info("in search for typ");
            grid.setItems(
                towarRepository
                .findByTypTowaruAndSklepId(rodzaj.getValue().getId(),sklepDto.getId())
                .stream()
                .map(towarMapper::toDto)
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
            new ComponentRenderer<>(towar -> new VButton(VaadinIcon.ARROW_RIGHT.create(),  clickEvent -> orderItem(towar)))
        )
        .setHeader("Zamow")
        .setAutoWidth(true);
        grid.addColumn(
            new ComponentRenderer<>(towar -> {
                VButton editButton = new VButton(VaadinIcon.EDIT.create(), clickEvent -> openTowarEditor(towar));
                VButton deleteButton = new VButton(VaadinIcon.TRASH.create(), clickEvent -> deleteTowar(towar));
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
        grid.setItems(towarRepository.findBySklepId(sklepDto.getId()).stream().map(towarMapper::toDto).toList());
        nazwa.setValue("");
    }
    private void openTowarEditor(TowarDto towarDto) {
        DialogTemplate dlg = new DialogTemplate(
                towarDto.getId() == null ? "Dodaj nowy towar" : "Modyfikuj dane towaru"
        );
        TowarEditor editor = new TowarEditor();
        editor.setSavedHandler(
            (AbstractForm.SavedHandler<TowarDto>) towarDtoHandler -> {
                BinderValidationStatus<TowarDto> status = editor.getBinder().validate();
                if (!status.isOk()) {
                    Notification.show(
                        "Proszę sprawdzić dane - nie wszystkie pola są wypełnione poprawnie!",
                        3000,
                        Notification.Position.MIDDLE
                    );
                }  else {
                    try {
                        entrySaved(towarDtoHandler);
                        dlg.close();
                    } catch (Exception ex) {
                        log.error("Error: {}", ex.getMessage(), ex);
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
            (AbstractForm.ResetHandler<TowarDto>) investorDto ->
                log.info("user have closed window without saving their data....")
        );
        editor.setEntity(towarDto);
        editor.getResetButton().setEnabled(true);
        editor.getResetButton()
                .addClickListener(l -> dlg.close());
        editor.getSaveButton().setEnabled(true);
        editor.getDeleteButton().setVisible(false);
        dlg.addContent(editor);
        dlg.open();
    }
    private void entrySaved(TowarDto clone) {
        log.info("entrySaved for {}....{}", TowarDto.class.getSimpleName(), clone);
        TowarDto managed = towarMapper.toDto(towarRepository.save(towarMapper.toEntity(clone)));
        log.info("Sklep saved: {}", managed);
        selectAllFromDb();
    }
    private void deleteTowar(TowarDto towarDto) {
        log.info("Deleting Sklep: {}", towarDto);
        towarRepository.deleteById(towarDto.getId());
        selectAllFromDb();
        log.info("Sklep with ID {} deleted", towarDto.getId());
    }
    private void zamow(TowarDto towarDto) {

    }
}