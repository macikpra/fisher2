package edu.store.vaadin.ui.sprzedawca;

import static edu.store.utils.ValidationUtils.hasRole;

import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.store.vaadin.ui.MainLayout;
import edu.store.vaadin.ui.kierownik.KierwonikZarzadzaniePracownikTabView;
import edu.store.vaadin.ui.services.BeanUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import java.io.Serial;
import org.vaadin.firitin.components.html.VDiv;

//@RolesAllowed("ROLE_Sprzedawca")
@PageTitle("Sprzedawca ")
@Route(value = "sprzedawca", layout = MainLayout.class)
@PermitAll
public class SprzedawcaMainView extends VDiv {

    @Serial
    private static final long serialVersionUID = -355494436140704630L;

    public SprzedawcaMainView() {
        getElement().getStyle().set("overflow", "auto");
        setSizeFull();

        if (!hasRole("Sprzedawca")) {
            return;
        }

        TabSheet tabSheet = new TabSheet();
        tabSheet.add("sprzedaż sprzętu", BeanUtil.createComponent(SprzedawcaSprzedazTabView.class));
        tabSheet.add("sprzedaż zezwoleń", BeanUtil.createComponent(SprzedawcaSprzedazZezwolenTabView.class));
        tabSheet.add("wydanie sprzętu", BeanUtil.createComponent(SprzedawcaWydanieSprzetuTabView.class));
        tabSheet.add("złożenie zamówienia", BeanUtil.createComponent(SprzedawcaZlozenieZamowieniaTabView.class));
        tabSheet.add("weryfikacja klienta", BeanUtil.createComponent(SprzedawcaWeryfikacjaKlientaTabView.class));
        add(tabSheet);
        tabSheet.setSizeFull();
    }
}
