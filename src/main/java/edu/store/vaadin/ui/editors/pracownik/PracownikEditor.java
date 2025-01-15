package edu.store.vaadin.ui.editors.pracownik;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import edu.store.database.entities.CfgRole;
import edu.store.database.entities.PracownikRola;
import edu.store.database.repositories.CfgRoleRepository;
import edu.store.database.repositories.PracownikRolaRepository;
import edu.store.model.EModus;
import edu.store.model.dto.PracownikDto;
import edu.store.vaadin.ui.services.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.formlayout.VFormLayout;
import org.vaadin.firitin.components.textfield.VBigDecimalField;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

public class PracownikEditor extends AbstractForm<PracownikDto> {

    private final transient Logger logger = LoggerFactory.getLogger(PracownikEditor.class);
    private final VTextField imie = new VTextField("Imie");
    private final VTextField nazwisko = new VTextField("nazwisko");

    private final VBigDecimalField pesel = new VBigDecimalField("pesel");

    private final VTextField telefon = new VTextField("telefon");

    private final VComboBox<CfgRole> rolesCbx = new VComboBox<>("Role");

    CfgRoleRepository cfgRoleRepository = BeanUtil.getBean(CfgRoleRepository.class);

    private final PracownikRolaRepository pracownikRolaRepository;

    private PracownikDto entity;
    private EModus modus;

    public PracownikEditor(PracownikRolaRepository pracownikRolaRepository) {
        super(PracownikDto.class);
        this.pracownikRolaRepository = pracownikRolaRepository;
        getElement().getStyle().set("overflow", "auto");
        setSaveCaption("Zapisz");
        setCancelCaption("Zamknij");

        logger.debug("{} created", PracownikEditor.class.getSimpleName());
    }

    @Override
    protected Component createContent() {
        Div panel = new Div();
        panel.setSizeFull();

        imie.setClearButtonVisible(true);
        nazwisko.setClearButtonVisible(true);
        pesel.setClearButtonVisible(true);
        telefon.setClearButtonVisible(true);
        rolesCbx.setClearButtonVisible(true);
        rolesCbx.setRequired(true);
        rolesCbx.setItemLabelGenerator(CfgRole::getName);

        rolesCbx.setItems(cfgRoleRepository.findAll());

        VFormLayout contentLayout = new VFormLayout(imie, nazwisko, pesel, telefon, rolesCbx);

        panel.add(contentLayout.withMargin(true), getToolbar());
        contentLayout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("25em", 1),
            new FormLayout.ResponsiveStep("32em", 2),
            new FormLayout.ResponsiveStep("32em", 3)
        );

        return panel;
    }

    public void setModus(EModus modus) {
        this.modus = modus;
        if (modus == EModus.READ_ONLY) {
            getSaveButton().setVisible(false);

            imie.setReadOnly(true);
            nazwisko.setReadOnly(true);
            pesel.setReadOnly(true);
            telefon.setReadOnly(true);
        } else {
            getSaveButton().setVisible(true);

            if (modus == EModus.CREATE) {
                imie.setReadOnly(false);
                nazwisko.setReadOnly(false);
                pesel.setReadOnly(false);
                telefon.setReadOnly(false);
            }
        }
    }

    @Override
    public void setEntity(PracownikDto entity) {
        super.setEntity(entity);
        this.entity = entity;

        logger.info("setEntity entered for PracownikDto");
        BeanValidationBinder<PracownikDto> entityBinder = new BeanValidationBinder<>(PracownikDto.class);
        entityBinder.forField(imie).asRequired().bind(PracownikDto::getImie, PracownikDto::setImie);
        entityBinder.forField(nazwisko).asRequired().bind(PracownikDto::getNazwisko, PracownikDto::setNazwisko);

        entityBinder.addStatusChangeListener(l -> logger.info("entityBinder.addStatusChangeListener: " + isValid()));
        entityBinder.addValueChangeListener(l -> logger.info("entityBinder.addValueChangeListener: " + isValid()));

        setBinder(entityBinder);
        if (entity.getId() != null) {
            PracownikRola pracownikRola = pracownikRolaRepository.findByPracownikId(entity.getId());
            if (pracownikRola != null && pracownikRola.getCfgRoleId() != null) {
                CfgRole role = cfgRoleRepository.findOneById(pracownikRola.getCfgRoleId());
                rolesCbx.setValue(role);
            }
        }
        logger.info("setEntity end for PracownikDto");
    }

    public CfgRole getSelectedRole() {
        return rolesCbx.getValue();
    }
}
