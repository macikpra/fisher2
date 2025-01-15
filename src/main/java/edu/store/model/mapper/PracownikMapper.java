package edu.store.model.mapper;

import edu.store.database.entities.Pracownik;
import edu.store.model.dto.PracownikDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PracownikMapper {
    PracownikDto toDto(Pracownik entity);
    Pracownik toEntity(PracownikDto dto);
}
