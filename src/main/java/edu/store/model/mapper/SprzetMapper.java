package edu.store.model.mapper;

import edu.store.database.entities.Sprzet;
import edu.store.model.dto.SprzetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SprzetMapper {
    SprzetDto toDto(Sprzet entity);
    Sprzet toEntity(SprzetDto dto);
}
