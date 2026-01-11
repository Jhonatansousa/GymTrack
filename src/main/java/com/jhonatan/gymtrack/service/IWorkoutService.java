package com.jhonatan.gymtrack.service;

import com.jhonatan.gymtrack.dto.WorkoutDivisionDTO;
import com.jhonatan.gymtrack.dto.WorkoutDivisionResponseDTO;

import java.util.List;

public interface IWorkoutService {
    WorkoutDivisionResponseDTO createDivision(WorkoutDivisionDTO workoutDivisionDTO);

    List<WorkoutDivisionResponseDTO> getAllDivisions();

}

