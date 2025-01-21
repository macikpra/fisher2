package edu.store.vaadin.ui.editors.towar;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import edu.store.database.entities.TypSprzetu;
import edu.store.database.entities.TypTowaru;
import edu.store.database.repositories.TypTowaruRepository;
import edu.store.model.dto.SklepDto;
import edu.store.model.dto.TowarDto;
import edu.store.vaadin.ui.services.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.firitin.components.combobox.VComboBox;
import org.vaadin.firitin.components.formlayout.VFormLayout;
import org.vaadin.firitin.components.textfield.VBigDecimalField;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

public class TowarEditor extends AbstractForm<TowarDto> {
    private final transient Logger logger = LoggerFactory.getLogger(TowarEditor.class);
    private final VTextField nazwa = new VTextField("Nazwa");
    private final VComboBox<TypTowaru> typ = new VComboBox<>("Typ");
    private final VTextField ilosc = new VTextField("Ilosc");
    private final VTextField cena = new VTextField("Cena");
    TypTowaruRepository typTowaruRepository = BeanUtil.getBean(TypTowaruRepository.class);
    private TowarDto entity;
    public TowarEditor() {
        super(TowarDto.class);
        getElement().getStyle().set("overflow", "auto");
        setSaveCaption("Zapisz");
        setCancelCaption("Zamknij");

        logger.debug("{} created", TowarEditor.class.getSimpleName());
    }
    @Override
    protected Component createContent(){
        Div panel = new Div();
        panel.setSizeFull();
        nazwa.setClearButtonVisible(true);
        nazwa.setRequired(true);
        typ.setClearButtonVisible(true);
        typ.setItemLabelGenerator(TypTowaru::getNazwa);
        typ.setItems(typTowaruRepository.findAll());
        typ.setValue(typTowaruRepository.findAll().get(0));
        ilosc.setClearButtonVisible(true);
        ilosc.setRequired(true);
        cena.setClearButtonVisible(true);
        cena.setRequired(true);
        VFormLayout contentLayout = new VFormLayout(nazwa,typ,ilosc,cena);
        panel.add(contentLayout.withMargin(true), getToolbar());
        contentLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("32em", 3)
        );

        return panel;
    }
    @Override
    public void setEntity(TowarDto entity){
        super.setEntity(entity);
        this.entity = entity;
        logger.info("setEntity entered for TowarDto");
        SklepDto sklepDto = UI.getCurrent().getSession().getAttribute(SklepDto.class);
        if (sklepDto != null) {
            entity.setSklepId(sklepDto.getId());
            logger.info("Setting SklepId from session: {}", sklepDto.getId());
        } else {
            logger.warn("SklepDto not found in session!");
        }
        typ.addValueChangeListener(event -> {
            TypTowaru selectedTyp = event.getValue();
            if (selectedTyp != null) {
                entity.setTypTowaru(selectedTyp.getId());
            }
        });
        TypTowaru selectedTyp = typ.getValue();
        if (selectedTyp != null) {
            entity.setTypTowaru(selectedTyp.getId());
        }
        BeanValidationBinder<TowarDto> entityBinder = new BeanValidationBinder<>(TowarDto.class);
        entityBinder.forField(nazwa).asRequired().bind(TowarDto::getNazwa, TowarDto::setNazwa);
        entityBinder.forField(cena).asRequired()
                .withConverter(new StringToFloatConverter("Please enter a valid number"))
                .bind(TowarDto::getCena, TowarDto::setCena);
        entityBinder.forField(ilosc).asRequired()
                .withConverter(new StringToLongConverter("Please enter a valid number"))
                .bind(TowarDto::getIlosc, TowarDto::setIlosc);
        entityBinder.addStatusChangeListener(l -> logger.info("entityBinder.addStatusChangeListener: {}", isValid()));
        entityBinder.addValueChangeListener(l -> logger.info("entityBinder.addValueChangeListener: {}", isValid()));
        setBinder(entityBinder);
        if (entity.getId() != null) {
            logger.info("Editing existing Towar with ID: {}", entity.getId());
        }
        logger.info("setEntity end for TowarDto");
    }
}
