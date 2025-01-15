package edu.store.vaadin.ui;

import static edu.store.utils.ValidationUtils.hasRole;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import edu.store.app.main.security.SecurityService;
import edu.store.app.main.security.SecurityUtils;
import edu.store.vaadin.ui.kierownik.KierownikMainView;
import edu.store.vaadin.ui.services.BeanUtil;
import edu.store.vaadin.ui.sprzedawca.SprzedawcaMainView;
import edu.store.vaadin.ui.store_user.NormalStoreUserView;
import java.io.Serial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.html.VLabel;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/app-layout.css", themeFor = "vaadin-app-layout")
public class MainLayout extends AppLayout {

    @Serial
    private static final long serialVersionUID = -6020602851527295075L;

    private final SecurityService securityService;

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        VLabel logo = new VLabel("Student Zone - Najlepsza aplikacja wędkarska w kraju i nie tylko...").withFullWidth();
        logo.addClassNames("header-white-font", LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Wyloguj " + u, e -> {
            System.out.println("1: is user logged in: " + SecurityUtils.isUserLoggedIn());
            System.out.println("1: user detials: " + SecurityUtils.getUserDetails());

            VaadinSession.getCurrent().getSession().invalidate();
            SecurityContextHolder.clearContext();
            SecurityContextHolder.getContext().setAuthentication(null);
            if (getUI().isPresent()) {
                getUI().get().getSession().close();
                getUI().get().getPage().getHistory().replaceState(null, "/");
                // RouteConfiguration.forSessionScope().
                getUI().get().navigate("/logout");
            }

            System.out.println("2: is user logged in: " + SecurityUtils.isUserLoggedIn());
            System.out.println("2: user detials: " + SecurityUtils.getUserDetails());
        });
        logout.setIcon(VaadinIcon.EXIT.create());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logout);
        header.setWidthFull();
        header.setVerticalComponentAlignment(FlexComponent.Alignment.END, logout);
        header.setWidthFull();
        header.addClassNames("header-blue", LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        VDiv div = new VDiv();
        div.setSizeFull();

        // Kierownik
        createKierownikNavigationView(div);
        createSprzedawcaNavigationView(div);
        createNormalUserNavigationView(div);
        addToDrawer(div);
    }

    private void createNormalUserNavigationView(VDiv div) {
        if (!hasRole("store_user")) {
            System.out.println("nie jest normal user!");
            return;
        }

        LeftNavigationMenuButton button = new LeftNavigationMenuButton(this, div);
        LeftNavigationComponentHolder holder = button.addContent(
            "left.nav.normal_user.main",
            new Image("images/shopping-cart-filled-32.png", "storeUser"),
            NormalStoreUserView.class,
            BeanUtil.getApplicationContext()
        );

        VVerticalLayout l = holder.getLayout();
        l.getStyle().set("cursor", "pointer");
        div.add(l);
    }

    private void createKierownikNavigationView(VDiv div) {
        if (!hasRole("kierownik")) {
            return;
        }

        LeftNavigationMenuButton button = new LeftNavigationMenuButton(this, div);
        LeftNavigationComponentHolder holder = button.addContent(
            "left.nav.kierownik.main",
            new Image("images/manager-32.png", "kierownik"),
            KierownikMainView.class,
            BeanUtil.getApplicationContext()
        );

        VVerticalLayout l = holder.getLayout();
        l.getStyle().set("cursor", "pointer");
        div.add(l);
    }

    private void createSprzedawcaNavigationView(VDiv div) {
        if (!hasRole("sprzedawca")) {
            return;
        }

        LeftNavigationMenuButton button = new LeftNavigationMenuButton(this, div);
        LeftNavigationComponentHolder holder = button.addContent(
            "left.nav.sprzedawca.main",
            new Image("images/store-32.png", "Sprzedawca"),
            SprzedawcaMainView.class,
            BeanUtil.getApplicationContext()
        );

        VVerticalLayout l = holder.getLayout();
        l.getStyle().set("cursor", "pointer");
        div.add(l);
    }
}
