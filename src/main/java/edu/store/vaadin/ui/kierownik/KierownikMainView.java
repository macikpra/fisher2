package edu.store.vaadin.ui.kierownik;

import static edu.store.utils.ValidationUtils.hasRole;

import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import edu.store.vaadin.ui.MainLayout;
import edu.store.vaadin.ui.services.BeanUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import java.io.Serial;
import org.springframework.web.context.annotation.SessionScope;
import org.vaadin.firitin.components.html.VDiv;

//@RolesAllowed("Kierownik")
@VaadinSessionScope
@SessionScope
@PageTitle("Kierownik ")
@Route(value = "kierownik", layout = MainLayout.class)
@PermitAll
public class KierownikMainView extends VDiv {

    @Serial
    private static final long serialVersionUID = 4924154115100206803L;

    public KierownikMainView() {
        getElement().getStyle().set("overflow", "auto");
        setSizeFull();

        if (!hasRole("Kierownik")) {
            return;
        }

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Pracownicy", BeanUtil.createComponent(KierwonikZarzadzaniePracownikTabView.class));
        tabSheet.add("Sklepy", BeanUtil.createComponent(KierwonikZarzadzanieSklepTabView.class));
        tabSheet.add("Statystki", BeanUtil.createComponent(KierwonikZarzadzanieStatystykaTabView.class));
        tabSheet.add("Sprzet i towar", BeanUtil.createComponent(KierwonikZarzadzanieSprzetTowarTabView.class));
        add(tabSheet);
        tabSheet.setSizeFull();
    }
}
