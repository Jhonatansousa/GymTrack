package com.jhonatan.gymtrack.mapper;

import com.jhonatan.gymtrack.dto.ExerciseDTO;
import com.jhonatan.gymtrack.dto.ExerciseResponseDTO;
import com.jhonatan.gymtrack.entity.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sets", ignore = true)
    @Mapping(target = "workoutDivision", ignore = true)
    Exercise toEntity(ExerciseDTO dto);

    @Mapping(target = "exerciseId", source = "id")
    @Mapping(target = "exerciseName", source = "name")
    @Mapping(target = "workoutDivisionId", source = "workoutDivision.id")
    ExerciseResponseDTO toDTO(Exercise exercise);
}
