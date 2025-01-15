package edu.store.model.dto;

import java.time.LocalDate;
import lombok.*;
import lombok.extern.java.Log;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Log
@ToString
public class UserRegistrationDto {

    private String imie;
    private String nazwisko;
    private String email;
    private String numerKartyWedkarskiej;
    private String numerTelefonu;
    private String password;
    private String password2;
    private LocalDate dataUrodzenia;
}
