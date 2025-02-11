package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.store.database.repositories.SklepRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.mapper.SklepMapper;
import edu.store.vaadin.ui.MainLayout;
import edu.store.vaadin.ui.common.dialog.DialogTemplate;
import edu.store.vaadin.ui.common.dialog.ErrorMessageDialog;
import edu.store.vaadin.ui.editors.sklep.SklepEditor;
import edu.store.vaadin.ui.services.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

@Route(value = "kierownikSklepTabView", layout = MainLayout.class)
@PageTitle("Kierownik Zarządzanie Sklepami")
@Slf4j
public class KierwonikZarzadzanieSklepTabView extends VDiv {
    private final VGrid<SklepDto> grid = new VGrid<>(SklepDto.class);
    private final VTextField adres = new VTextField("Adres");
    private final SklepRepository sklepRepository;
    private final SklepMapper sklepMapper;
    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());
    public KierwonikZarzadzanieSklepTabView(
            SklepRepository sklepRepository,
            SklepMapper sklepMapper
    ) {
        this.sklepRepository = sklepRepository;
        this.sklepMapper = sklepMapper;

        getElement().getStyle().set("overflow", "auto");
        setSizeFull();

        VButton addShop = new VButton(VaadinIcon.PLUS.create(), l -> openSklepEditor(new SklepDto()));
        VButton refresh = new VButton(VaadinIcon.REFRESH.create(), l -> selectAllFromDb());
        add(
                new VHorizontalLayout( adres, buttonSearch, refresh, addShop)
                        .withSpacing(true)
                        .withPadding(false)
                        .withMargin(false)
                        .withDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE)
        );
        initGrid();
        selectAllFromDb();
    }
    private void performSearchInGrid(){
        if(isAdresEntered()){
            log.info("Searching for adres containing: {}", adres.getValue());
            grid.setItems(
                    sklepRepository
                            .findByAdresContainingIgnoreCase(adres.getValue())
                            .stream()
                            .map(sklepMapper::toDto)
                            .toList()
            );
        }
    }
    private boolean isAdresEntered(){
        return !StringUtils.isEmpty(adres.getValue()) && adres.getValue().length() >= 3;
    }
    private void initGrid(){
        grid.setWidthFull();
        grid.addColumn(
            new ComponentRenderer<>(sklep -> new VButton("Sprawdz", VaadinIcon.EDIT.create(), clickEvent -> checkMagazyn(sklep)))
        )
        .setHeader("Stan magazynu")
        .setAutoWidth(true);
        grid.addColumn(
            new ComponentRenderer<>(sklep -> {
                VButton editButton = new VButton("Edytuj", VaadinIcon.EDIT.create(), clickEvent -> openSklepEditor(sklep));
                VButton deleteButton = new VButton("Usun", VaadinIcon.TRASH.create(), clickEvent -> deleteSklep(sklep));
                deleteButton.getStyle().set("color", "red");
                return new VHorizontalLayout(editButton, deleteButton);
            })
        )
        .setHeader("Akcje")
        .setAutoWidth(true);
        add(grid);
    }
    private void selectAllFromDb(){
        grid.setItems(sklepRepository.findAll().stream().map(sklepMapper::toDto).toList());
        adres.setValue("");
    }
    private void openSklepEditor(SklepDto sklepDto){
        DialogTemplate dlg = new DialogTemplate(
                sklepDto.getId() == null ? "Dodaj nowy sklep" : "Modyfikuj dane sklepu"
        );
        SklepEditor editor = new SklepEditor();
        editor.setSavedHandler(
            (AbstractForm.SavedHandler<SklepDto>) sklepDtoHandler -> {
            BinderValidationStatus<SklepDto> status = editor.getBinder().validate();
            if (!status.isOk()) {
                Notification.show(
                    "Proszę sprawdzić dane - nie wszystkie pola są wypełnione poprawnie!",
                    3000,
                    Notification.Position.MIDDLE
                );
            }  else {
                try {
                    entrySaved(sklepDtoHandler);
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
            (AbstractForm.ResetHandler<SklepDto>) investorDto ->
                log.info("user have closed window without saving their data....")
        );
        editor.setEntity(sklepDto);
        editor.getResetButton().setEnabled(true);
        editor.getResetButton()
              .addClickListener(l -> dlg.close());
        editor.getSaveButton().setEnabled(true);
        editor.getDeleteButton().setVisible(false);
        dlg.addContent(editor);
        dlg.open();
    }

    private void entrySaved(SklepDto clone) {
        log.info("entrySaved for {}....{}", SklepDto.class.getSimpleName(), clone);
        SklepDto managed = sklepMapper.toDto(sklepRepository.save(sklepMapper.toEntity(clone)));
        log.info("Sklep saved: {}", managed);
        selectAllFromDb();
    }
    private void deleteSklep(SklepDto sklepDto){
        log.info("Deleting Sklep: {}", sklepDto);
        sklepRepository.deleteById(sklepDto.getId());
        selectAllFromDb();
        log.info("Sklep with ID {} deleted", sklepDto.getId());
    }
    private void checkMagazyn(SklepDto sklepDto){
        UI.getCurrent().getSession().setAttribute(SklepDto.class, sklepDto);
        DialogTemplate dlg = new DialogTemplate(
                "Magazyn sklepu " + sklepDto.getAdres()
        );
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Sprzet", BeanUtil.createComponent(KierwonikZarzadzanieSprzetTabView.class));
        tabSheet.add("Towar",BeanUtil.createComponent(KierwonikZarzadzanieTowarTabView.class));
        dlg.addContent(tabSheet);
        dlg.open();
    }
}
