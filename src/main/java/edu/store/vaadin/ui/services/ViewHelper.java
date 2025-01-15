package edu.store.vaadin.ui.services;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VLabel;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

public class ViewHelper {

    public static VDiv getKachel(String imageFileName, String firstLabel, String secondLabel, String id) {
        VDiv div = new VDiv();
        VVerticalLayout divLayout = new VVerticalLayout().withPadding(true);
        divLayout.add(new Image(imageFileName, imageFileName));
        divLayout.add(new VLabel(firstLabel));
        divLayout.add(new VLabel(secondLabel));
        divLayout.getStyle().set("border", "0.1px dotted lightGrey");
        divLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        div.setId(id);
        div.add(divLayout);
        div.getStyle().setMargin("20px");

        return div;
    }
}
