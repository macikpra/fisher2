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
public class SprzetDto {
    private Long id;
    private Long sklepId;
    private String nazwa;
    private Long typSprzetu;
    private Float cena;
}
