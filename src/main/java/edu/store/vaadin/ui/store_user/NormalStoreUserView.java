package edu.store.vaadin.ui.store_user;

import static edu.store.utils.ValidationUtils.hasRole;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.store.vaadin.ui.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.vaadin.firitin.components.html.VDiv;

@PageTitle("Witamy w Sklepie Online ")
@Route(value = "store_user", layout = MainLayout.class)
@PermitAll
public class NormalStoreUserView extends VDiv {

    public NormalStoreUserView() {
        getElement().getStyle().set("overflow", "auto");
        setSizeFull();

        if (!hasRole("store_user")) {
            return;
        }
        add(new H1("Witamy na naszej stronie sklepu wedkarskiego!"));
    }
}
