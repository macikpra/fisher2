package edu.store.model.dto;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import lombok.*;
import lombok.extern.java.Log;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Log
@ToString
public class PracownikRolaDto {

    private Long id;
    private Long pracownikId;
    private Long cfgRoleId;
    private Date dataZatrudnienia;
    private Date dataZwolnienia;
}
