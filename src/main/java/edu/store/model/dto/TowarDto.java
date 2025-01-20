package edu.store.model.dto;

import jakarta.persistence.Column;
import lombok.*;
import lombok.extern.java.Log;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Log
@ToString
@EqualsAndHashCode
public class TowarDto {
    private Long id;
    private Long sklepId;
    private String nazwa;
    private Long typTowaru;
    private Long ilosc;
    private Float cena;
}
