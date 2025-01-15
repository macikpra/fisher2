package edu.store.model.dto;

import java.math.BigDecimal;
import lombok.*;
import lombok.extern.java.Log;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Log
@ToString
@EqualsAndHashCode
public class PracownikDto {

    private Long id;
    private String imie;
    private String nazwisko;
    private BigDecimal pesel;
    private String telefon;
    private String haslo_hash;
}
