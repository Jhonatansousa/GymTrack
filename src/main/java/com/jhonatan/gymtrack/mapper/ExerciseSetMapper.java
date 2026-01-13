package com.jhonatan.gymtrack.mapper;

import com.jhonatan.gymtrack.dto.ExerciseSetResponseDTO;
import com.jhonatan.gymtrack.entity.ExerciseSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseSetMapper {

    @Mapping(target = "exerciseId", source = "exercise.id")
    @Mapping(target = "exerciseSetId", source = "id")
    @Mapping(target = "setName", source = "name")
    ExerciseSetResponseDTO toDTO(ExerciseSet exerciseSet);
}
