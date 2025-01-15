package edu.store.vaadin.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.dom.Element;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.vaadin.firitin.components.accordion.VAccordionPanel;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

public class LeftNavigationMenuButton {

    private Div parent;
    private AppLayout app;
    private Map<Component, String> mapTranslations;

    private Logger logger = LoggerFactory.getLogger(LeftNavigationMenuButton.class);

    public LeftNavigationMenuButton() {}

    public LeftNavigationMenuButton(AppLayout appReference, Div parent) {
        this.app = appReference;
        this.parent = parent;
        mapTranslations = new HashMap<Component, String>();
    }

    public LeftNavigationComponentHolder addContent(
        String targetDescription,
        Image image,
        Class<? extends Component> navigationTarget,
        ApplicationContext applicationContext
    ) {
        LeftNavigationComponentHolder holder = new LeftNavigationComponentHolder();
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        Component c = beanFactory.createBean(navigationTarget);

        VVerticalLayout layout = new VVerticalLayout();

        logger.info("addContent::component: {}", c);
        layout.add(image);

        // Link
        Label link = new Label(layout.getTranslation(targetDescription));
        layout.add(link);
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        layout.addClickListener(e -> {
            app.setContent(c);
            updateBackGroundColorsForLinkAction(e.getSource().getElement(), parent);
        });

        mapTranslations.put(link, targetDescription);

        holder.setActionImage(image);
        holder.setContentComponent(c);
        holder.setNavigationTarget(navigationTarget);
        holder.setLayout(layout);
        // layout.addClassName("Rechteck-2");

        return holder;
    }

    private void updateBackGroundColorsForLinkAction(Element clickedElement, Div divElement) {
        Stream<Component> c = divElement.getChildren();
        c.filter(s -> s.getChildren().count() > 0).forEach(this::processComponent);
        clickedElement.getStyle().set("border", "1px dotted blue");
    }

    private void processComponent(Component c) {
        if ("VVerticalLayout".equalsIgnoreCase(c.getClass().getSimpleName())) {
            c.getElement().getStyle().set("border", "none");
        }

        c.getChildren().filter(s -> s.getChildren().count() > 0).forEach(this::processComponent);
    }

    public void updateLocale() {
        logger.debug(
            "LeftNavigationMenuButton locale changed in mainLayout and has been triggered from" + " outside...."
        );
        for (Component c : mapTranslations.keySet()) {
            if (c instanceof Label) {
                Label rl = (Label) c;
                rl.setText(rl.getTranslation(mapTranslations.get(c)));
            } else if (c instanceof VAccordionPanel) {
                VAccordionPanel p = (VAccordionPanel) c;
                p.setSummaryText(p.getTranslation(mapTranslations.get(c)));
            }
        }
    }
}
