package edu.store.model.mapper;

import edu.store.database.entities.PracownikRola;
import edu.store.model.dto.PracownikRolaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PracownikRolaMapper {
    PracownikRolaDto toDto(PracownikRola entity);
    PracownikRola toEntity(PracownikRolaDto dto);
}
