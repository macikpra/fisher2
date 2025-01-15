package edu.store.vaadin.ui.services;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import org.vaadin.firitin.components.html.VDiv;

public class LazyComponent extends VDiv {

    public LazyComponent(SerializableSupplier<? extends Component> supplier) {
        setSizeFull();
        Component c = supplier.get();
        if (c.getParent() != null) {
            System.out.println("LazyComponent component has parent!!!");
        }
        addAttachListener(e -> {
            if (getElement().getChildCount() == 0) {
                getElement().removeFromTree();
                add(c);
            }
        });
    }
}
