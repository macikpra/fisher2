package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import edu.store.database.entities.TypTowaru;
import edu.store.database.repositories.TowarRepository;
import edu.store.database.repositories.TypTowaruRepository;
import edu.store.model.dto.SklepDto;
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

import java.util.List;

@Slf4j
public class KierwonikZarzadzanieTowarTabView extends VDiv {
    private final VGrid<TowarDto> grid = new VGrid<>(TowarDto.class);
    private final VTextField nazwa = new VTextField("Nazwa");
    private final TowarRepository towarRepository;
    private final TowarMapper towarMapper;
    private final TypTowaruRepository typTowaruRepository;
    private final VComboBox<TypTowaru> rodzaj = new VComboBox<>("Rodzaj");
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
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
        add(
            new VHorizontalLayout( nazwa,rodzaj, buttonSearch, refresh, addTowar)
                .withSpacing(true)
                .withPadding(false)
                .withMargin(false)
                .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        initGrid();
        selectAllFromDb();
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