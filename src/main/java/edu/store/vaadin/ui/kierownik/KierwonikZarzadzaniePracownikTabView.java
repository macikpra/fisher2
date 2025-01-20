package edu.store.vaadin.ui.kierownik;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.store.database.entities.CfgRole;
import edu.store.database.entities.CfgUser;
import edu.store.database.entities.CfgUserRoleMap;
import edu.store.database.entities.PracownikRola;
import edu.store.database.repositories.*;
import edu.store.database.service.CfgUserService;
import edu.store.model.dto.PracownikDto;
import edu.store.model.mapper.PracownikMapper;
import edu.store.vaadin.ui.MainLayout;
import edu.store.vaadin.ui.common.dialog.DialogTemplate;
import edu.store.vaadin.ui.common.dialog.ErrorMessageDialog;
import edu.store.vaadin.ui.editors.pracownik.PracownikEditor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.html.VSpan;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

@PageTitle("Kierownik Zarzadzanie")
@Route(value = "kierwonikZarzadzanieTabView", layout = MainLayout.class)
@Slf4j
public class KierwonikZarzadzaniePracownikTabView extends VVerticalLayout {

    private final VGrid<PracownikDto> grid = new VGrid<>(PracownikDto.class);
    private final VTextField nazwisko = new VTextField("Nazwisko");
    private final VTextField imie = new VTextField("Imie");
    private final PracownikRepository pracownikRepository;
    private final PracownikRolaRepository pracownikRolaRepository;

    private final CfgRoleRepository cfgRoleRepository;
    private final CfgUserService cfgUserService;

    private final PracownikMapper pracownikMapper;

    private final VButton buttonSearch = new VButton(VaadinIcon.SEARCH.create(), "Szukaj", l -> performSearchInGrid());

    public KierwonikZarzadzaniePracownikTabView(
        PracownikRepository pracownikRepository,
        PracownikRolaRepository pracownikRolaRepository,
        CfgRoleRepository cfgRoleRepository,
        CfgUserService cfgUserService,
        PracownikMapper pracownikMapper
    ) {
        this.pracownikRepository = pracownikRepository;
        this.pracownikRolaRepository = pracownikRolaRepository;
        this.cfgRoleRepository = cfgRoleRepository;
        this.cfgUserService = cfgUserService;
        this.pracownikMapper = pracownikMapper;

        getElement().getStyle().set("overflow", "auto");
        setSizeFull();

        VButton addInvestor = new VButton(VaadinIcon.PLUS.create(), l -> openPracownikEditor(new PracownikDto()));
        VButton refresh = new VButton(VaadinIcon.REFRESH.create(), l -> selectAllFromDb());
        add(
            new VHorizontalLayout( nazwisko, imie, buttonSearch, refresh, addInvestor)
                .withSpacing(true)
                .withPadding(false)
                .withMargin(false)
                .withDefaultVerticalComponentAlignment(Alignment.BASELINE)
        );
        initGrid();
        selectAllFromDb();
    }

    private void performSearchInGrid() {
        if (isImieEntered() && isNazwiskoEntered()) {
            log.info("in search for both city and lastName");
            grid.setItems(
                pracownikRepository
                    .findByImieContainingIgnoreCaseAndNazwiskoContainingIgnoreCase(imie.getValue(), nazwisko.getValue())
                    .stream()
                    .map(pracownikMapper::toDto)
                    .toList()
            );
        } else if (isImieEntered()) {
            log.info("in search only for city");
            grid.setItems(
                pracownikRepository
                    .findByImieContainingIgnoreCase(imie.getValue())
                    .stream()
                    .map(pracownikMapper::toDto)
                    .toList()
            );
        } else if (isNazwiskoEntered()) {
            log.info("in search only for lastName");
            grid.setItems(
                pracownikRepository
                    .findByNazwiskoContainingIgnoreCase(nazwisko.getValue())
                    .stream()
                    .map(pracownikMapper::toDto)
                    .toList()
            );
        }
    }

    private boolean isImieEntered() {
        return !StringUtils.isEmpty(imie.getValue()) && imie.getValue().length() >= 3;
    }

    private boolean isNazwiskoEntered() {
        return !StringUtils.isEmpty(nazwisko.getValue()) && nazwisko.getValue().length() >= 3;
    }

    private void initGrid() {
        grid.setWidthFull();
        Grid.Column<PracownikDto> col = grid
        .addColumn(
            new ComponentRenderer<>(c -> {
                PracownikRola pracownikRola = pracownikRolaRepository.findByPracownikId(c.getId());
                if (pracownikRola != null && pracownikRola.getCfgRoleId() != null) {
                    CfgRole role = cfgRoleRepository.findOneById(pracownikRola.getCfgRoleId());
                    VSpan label = new VSpan(role.getName());
                    return new VHorizontalLayout(label);
                }
                VSpan label = new VSpan("Rola nieznana");
                return new VHorizontalLayout(label);
            })
        )
        .setSortable(true)
        .setHeader("Rola pracownika");
        col.setId("Rola");
        grid.removeColumnByKey("haslo_hash");
        grid.addColumn(
            new ComponentRenderer<>(pracownik -> {
                VButton editButton = new VButton("Edytuj", VaadinIcon.EDIT.create(), clickEvent -> openPracownikEditor(pracownik));
                VButton deleteButton = new VButton("Usun", VaadinIcon.TRASH.create(), clickEvent -> deletePracownik(pracownik));
                deleteButton.getStyle().set("color", "red");
                return new VHorizontalLayout(editButton, deleteButton);
            })
        )
        .setHeader("Akcje")
        .setAutoWidth(true);
        add(grid);
    }

    private void selectAllFromDb() {
        grid.setItems(pracownikRepository.findAll().stream().map(pracownikMapper::toDto).toList());
        nazwisko.setValue("");
        imie.setValue("");
    }

    private void openPracownikEditor(PracownikDto pracownikDto) {
        DialogTemplate dlg = new DialogTemplate(
            pracownikDto.getId() == null ? "Dodaj nowego pracownika" : "Modyfikuj dane pracownika"
        );
        PracownikEditor editor = new PracownikEditor(pracownikRolaRepository);
        editor.setSavedHandler(
            (AbstractForm.SavedHandler<PracownikDto>) pracownikDtoHandler -> {
                BinderValidationStatus<PracownikDto> status = editor.getBinder().validate();
                if (!status.isOk()) {
                    Notification.show(
                        "Proszę sprawdzić dane - nie wszystkie pola są wypełnione poprawnie!",
                        3000,
                        Notification.Position.MIDDLE
                    );
                } else if (editor.getSelectedRole() == null || "Kierownik".equals(editor.getSelectedRole().getName())) {
                    Notification.show(
                        "Proszę podac role dla pracownika! Role nie moze byc Kierownikiem!",
                        3000,
                        Notification.Position.MIDDLE
                    );
                } else {
                    try {
                        entrySaved(pracownikDtoHandler, editor);
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
            (AbstractForm.ResetHandler<PracownikDto>) investorDto ->
                log.info("user have closed window without saving their data....")
        );
        editor.setEntity(pracownikDto);
        editor.getResetButton().setEnabled(true);
        editor
            .getResetButton()
            .addClickListener(l -> dlg.close());
        editor.getSaveButton().setEnabled(true);
        editor.getDeleteButton().setVisible(false);
        dlg.addContent(editor);
        dlg.open();
    }

    private void entrySaved(PracownikDto clone, PracownikEditor editor) {
        log.info("entrySaved for {}....{}", PracownikDto.class.getSimpleName(), clone);
        PracownikDto managed = pracownikMapper.toDto(pracownikRepository.save(pracownikMapper.toEntity(clone)));

        PracownikRola rolaToCheck = pracownikRolaRepository.findByPracownikId(managed.getId());
        PracownikRola pracownikRola = rolaToCheck == null ? new PracownikRola() : rolaToCheck;
        pracownikRola.setPracownikId(managed.getId());
        pracownikRola.setCfgRoleId(editor.getSelectedRole().getId());
        PracownikRola savedRola = pracownikRolaRepository.save(pracownikRola);

        // create pracownik for login...default password for all new users: 1234567
        Pair<CfgUser, CfgUserRoleMap> ret = cfgUserService.createNewUser(
            managed.getImie(),
            managed.getNazwisko(),
            "1234567",
            "" + managed.getPesel(),
            managed.getTelefon(),
            editor.getSelectedRole().getName()
        );
        log.info("user created: " + ret);
        selectAllFromDb();
    }
    private void deletePracownik(PracownikDto pracownikDto) {
        log.info("Deleting pracownik: {}", pracownikDto);
        pracownikRolaRepository.deleteById(pracownikDto.getId());
        pracownikRepository.deleteById(pracownikDto.getId());
        selectAllFromDb();
        log.info("Pracownik with ID {} deleted", pracownikDto.getId());
    }
}
