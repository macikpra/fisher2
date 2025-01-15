package edu.store.vaadin.ui.register;

import static edu.store.utils.ValidationUtils.passwordIsValid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.store.database.entities.CfgUser;
import edu.store.database.entities.CfgUserRoleMap;
import edu.store.database.service.CfgUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.datepicker.VDatePicker;
import org.vaadin.firitin.components.formlayout.VFormLayout;
import org.vaadin.firitin.components.html.VDiv;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.textfield.VTextField;

@PageTitle("Rejestracja ")
@Route(value = "register")
@AnonymousAllowed
@Slf4j
public class RegisterMainView extends VHorizontalLayout {

    @Autowired
    CfgUserService cfgUserService;

    VTextField imie = new VTextField("Imię: ");
    VTextField nazwisko = new VTextField("Nazwisko: ");
    VDatePicker dataUrodzenia = new VDatePicker("Data urodzenia: ");
    VTextField email = new VTextField("E-Mail: ");
    VTextField numerKartyWedkarskiej = new VTextField("Numer karty wędkarskiej: ");

    VTextField numerTelefonu = new VTextField("Numer telefonu: ");

    VTextField password = new VTextField("Hasło:");
    VTextField password2 = new VTextField("Potwierz hasło:");

    public RegisterMainView() {
        super();
        // Widgets
        this.add(createRegisterForm());

        this.setWidth("50%");
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Put content in the middle horizontally.
        this.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Component createRegisterForm() {
        imie.setRequired(true);
        imie.setRequiredIndicatorVisible(true);

        nazwisko.setRequired(true);
        nazwisko.setRequiredIndicatorVisible(true);

        dataUrodzenia.setRequired(true);
        dataUrodzenia.setRequiredIndicatorVisible(true);

        email.setRequired(true);
        email.setRequiredIndicatorVisible(true);

        numerKartyWedkarskiej.setRequired(true);
        numerKartyWedkarskiej.setRequiredIndicatorVisible(true);

        numerTelefonu.setRequired(true);
        numerTelefonu.setRequiredIndicatorVisible(true);

        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);

        password2.setRequired(true);
        password2.setRequiredIndicatorVisible(true);

        VButton register = new VButton("Register", VaadinIcon.PENCIL.create(), l -> {
            if (
                StringUtils.isAnyEmpty(
                    imie.getValue(),
                    nazwisko.getValue(),
                    email.getValue(),
                    numerKartyWedkarskiej.getValue(),
                    numerTelefonu.getValue(),
                    password.getValue(),
                    password2.getValue()
                ) ||
                dataUrodzenia.getValue() == null
            ) {
                Notification.show("Wszystkie pola musza byc wypelnione!", 2000, Notification.Position.MIDDLE);
                return;
            } else if (!password.getValue().equals(password2.getValue())) {
                Notification.show("Hasła nie sa identyczne!", 2000, Notification.Position.MIDDLE);
                return;
            } else if (!passwordIsValid(password.getValue())) {
                Notification.show(
                    "Hasło musi zawierac minimum 6 znaków, wymagana przynajmniej jedna litera i jedna cyfra!",
                    2000,
                    Notification.Position.MIDDLE
                );
                return;
            } else if (emailAlreadyPresent(email.getValue())) {
                Notification.show("Uzytkownik o podanym E-Mail juz istnieje!", 2000, Notification.Position.MIDDLE);
                return;
            }

            registerUser();
        });

        VDiv div = new VDiv();
        VFormLayout formLayout = new VFormLayout(
            imie,
            nazwisko,
            dataUrodzenia,
            email,
            numerKartyWedkarskiej,
            numerTelefonu,
            password,
            password2,
            register
        );
        formLayout.setSizeUndefined();
        div.add(formLayout);

        div
            .getStyle()
            .set("display", "flex")
            .set("justify-content", "center")
            .set("align-items", "center")
            .set("text-align", "center")
            .set("margin-left", "20%")
            .set("margin-top", "50px");
        return div;
    }

    private boolean emailAlreadyPresent(String value) {
        RestTemplate rt = new RestTemplate();
        ResponseEntity<Boolean> userExists = rt.getForEntity(
            ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/checkUser?email=" + value,
            Boolean.class
        );

        return Boolean.TRUE.equals(userExists.getBody());
    }

    private void registerUser() {
        Pair<CfgUser, CfgUserRoleMap> ret = cfgUserService.createNewUser(
            imie.getValue(),
            nazwisko.getValue(),
            password.getValue(),
            numerKartyWedkarskiej.getValue(),
            numerTelefonu.getValue(),
            "store_user"
        );
        log.debug("user created: " + ret);
        UI.getCurrent().navigate("/login");
    }
}
