package edu.store.model.mapper;

import edu.store.database.entities.Towar;
import edu.store.model.dto.TowarDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TowarMapper {
    TowarDto toDto(Towar entity);
    Towar toEntity(TowarDto dto);
}
