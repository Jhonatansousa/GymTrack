package com.jhonatan.gymtrack.mapper;

import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;
import com.jhonatan.gymtrack.entity.WorkoutDivision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutDivisionMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "exercises", ignore = true)
    //id é importante ignorar pq é criado no banco na hora da persistência
    @Mapping(target = "id",  ignore = true)
    WorkoutDivision toEntity(WorkoutDivisionDTO dto);

    WorkoutDivisionResponseDTO toDTO(WorkoutDivision workoutDivision);
}
