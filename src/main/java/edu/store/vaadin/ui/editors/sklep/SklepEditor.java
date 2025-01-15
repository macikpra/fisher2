package edu.store.vaadin.ui.editors.sklep;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import edu.store.model.dto.SklepDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.firitin.components.formlayout.VFormLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;

public class SklepEditor  extends AbstractForm<SklepDto> {
    private final transient Logger logger = LoggerFactory.getLogger(SklepEditor.class);
    private final VTextField adres = new VTextField("Adres");
    private SklepDto entity;
    public SklepEditor() {
        super(SklepDto.class);
        getElement().getStyle().set("overflow", "auto");
        setSaveCaption("Zapisz");
        setCancelCaption("Zamknij");

        logger.debug("{} created", SklepEditor.class.getSimpleName());
    }
    @Override
    protected Component createContent(){
        Div panel = new Div();
        panel.setSizeFull();
        adres.setClearButtonVisible(true);
        adres.setRequired(true);
        VFormLayout contentLayout = new VFormLayout(adres);
        panel.add(contentLayout.withMargin(true), getToolbar());
        contentLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("32em", 3)
        );

        return panel;
    }
    @Override
    public void setEntity(SklepDto entity){
        super.setEntity(entity);
        this.entity = entity;
        logger.info("setEntity entered for SklepDto");
        BeanValidationBinder<SklepDto> entityBinder = new BeanValidationBinder<>(SklepDto.class);
        entityBinder.forField(adres).asRequired().bind(SklepDto::getAdres, SklepDto::setAdres);
        entityBinder.addStatusChangeListener(l -> logger.info("entityBinder.addStatusChangeListener: " + isValid()));
        entityBinder.addValueChangeListener(l -> logger.info("entityBinder.addValueChangeListener: " + isValid()));
        setBinder(entityBinder);
        if (entity.getId() != null) {
            logger.info("Editing existing Sklep with ID: " + entity.getId());
        }
        logger.info("setEntity end for SklepDto");
    }
}
