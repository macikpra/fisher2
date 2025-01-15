package edu.store.vaadin.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.io.Serial;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;

@Route("login")
@PageTitle("Login | Sklep")
@PermitAll
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Serial
    private static final long serialVersionUID = -4345502791724453273L;

    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Proszę się zalogować");
        i18nForm.setUsername("nazwa użytkownika:");
        i18nForm.setPassword("hasło:");
        i18nForm.setSubmit("Wyślij");
        i18nForm.setForgotPassword("Zapomniałeś hasła?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("wystąpił błąd!");
        i18nErrorMessage.setMessage("Popraw dane!");
        i18n.setErrorMessage(i18nErrorMessage);

        login.setI18n(i18n);
        login.setAction("login");

        VButton button = new VButton("Zarejestruj sie...", VaadinIcon.USER_CHECK.create(), l -> {
            getUI().ifPresent(ui -> ui.navigate("/register"));
        });

        add(new H1("Sklep Wędkarski"), new Image("images/fish.png", "LOGO"), login, button);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}
