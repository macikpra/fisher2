package edu.store.vaadin.ui.editors.sprzet;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import edu.store.database.entities.TypSprzetu;
import edu.store.database.repositories.TypSprzetuRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.dto.SprzetDto;
import edu.store.vaadin.ui.services.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.formlayout.VFormLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

public class SprzetEditor extends AbstractForm<SprzetDto> {
    private final transient Logger logger = LoggerFactory.getLogger(SprzetEditor.class);
    private final VTextField nazwa = new VTextField("Nazwa");
    private final VComboBox<TypSprzetu> typ = new VComboBox<>("Typ");
    private final VTextField cena = new VTextField("Cena");
    TypSprzetuRepository typSprzetuRepository = BeanUtil.getBean(TypSprzetuRepository.class);
    private SprzetDto entity;
    public SprzetEditor() {
        super(SprzetDto.class);
        getElement().getStyle().set("overflow", "auto");
        setSaveCaption("Zapisz");
        setCancelCaption("Zamknij");

        logger.debug("{} created", SprzetEditor.class.getSimpleName());
    }
    @Override
    protected Component createContent(){
        Div panel = new Div();
        panel.setSizeFull();
        nazwa.setClearButtonVisible(true);
        nazwa.setRequired(true);
        typ.setClearButtonVisible(true);
        typ.setItemLabelGenerator(TypSprzetu::getNazwa);
        typ.setItems(typSprzetuRepository.findAll());
        typ.setValue(typSprzetuRepository.findAll().get(0));
        cena.setClearButtonVisible(true);
        cena.setRequired(true);
        VFormLayout contentLayout = new VFormLayout(nazwa,typ,cena);
        panel.add(contentLayout.withMargin(true), getToolbar());
        contentLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("32em", 3)
        );

        return panel;
    }
    @Override
    public void setEntity(SprzetDto entity){
        super.setEntity(entity);
        this.entity = entity;
        logger.info("setEntity entered for SprzetDto");
        SklepDto sklepDto = UI.getCurrent().getSession().getAttribute(SklepDto.class);
        if (sklepDto != null) {
            entity.setSklepId(sklepDto.getId()); // Set the Sklep ID in TowarDto
            logger.info("Setting SklepId from session: {}", sklepDto.getId());
        } else {
            logger.warn("SklepDto not found in session!");
        }
        typ.addValueChangeListener(event -> {
            TypSprzetu selectedTyp = event.getValue();
            if (selectedTyp != null) {
                entity.setTypSprzetu(selectedTyp.getId());
            }
        });
        TypSprzetu selectedTyp = typ.getValue();
        if (selectedTyp != null) {
            entity.setTypSprzetu(selectedTyp.getId());
        }
        BeanValidationBinder<SprzetDto> entityBinder = new BeanValidationBinder<>(SprzetDto.class);
        entityBinder.forField(nazwa).asRequired().bind(SprzetDto::getNazwa, SprzetDto::setNazwa);
        entityBinder.forField(cena).asRequired()
                .withConverter(new StringToFloatConverter("Please enter a valid number"))
                .bind(SprzetDto::getCena, SprzetDto::setCena);
        entityBinder.addStatusChangeListener(l -> logger.info("entityBinder.addStatusChangeListener: {}", isValid()));
        entityBinder.addValueChangeListener(l -> logger.info("entityBinder.addValueChangeListener: {}", isValid()));
        setBinder(entityBinder);
        if (entity.getId() != null) {
            logger.info("Editing existing Sprzet with ID: {}", entity.getId());
        }
        logger.info("setEntity end for SprzetDto");
    }
}
