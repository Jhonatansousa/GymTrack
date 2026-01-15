package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.workoutdivision.DivisionUpdateDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.workoutdivision.WorkoutDivisionResponseDTO;

import java.util.List;

public interface IWorkoutService {
    WorkoutDivisionResponseDTO createDivision(WorkoutDivisionDTO workoutDivisionDTO);

    List<WorkoutDivisionResponseDTO> getAllDivisions();

    WorkoutDivisionResponseDTO updateDivision(DivisionUpdateDTO divisionUpdateDTO);

    void deleteDivision(long divisionId);

}

