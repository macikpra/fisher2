package edu.store.model.dto;

import lombok.*;
import lombok.extern.java.Log;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Log
@ToString
@EqualsAndHashCode
public class SklepDto {
    private Long id;
    private String adres;
}
