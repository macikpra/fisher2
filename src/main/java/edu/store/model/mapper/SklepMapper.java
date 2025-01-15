package edu.store.model.mapper;

import edu.store.database.entities.Sklep;
import edu.store.model.dto.SklepDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SklepMapper {
    SklepDto toDto(Sklep entity);
    Sklep toEntity(SklepDto dto);
}
